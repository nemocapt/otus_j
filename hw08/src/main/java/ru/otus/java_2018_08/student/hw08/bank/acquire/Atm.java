package ru.otus.java_2018_08.student.hw08.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw08.bank.Account;
import ru.otus.java_2018_08.student.hw08.bank.Bank;
import ru.otus.java_2018_08.student.hw08.common.Banknote;
import ru.otus.java_2018_08.student.hw08.common.Currency;
import ru.otus.java_2018_08.student.hw08.common.Money;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class Atm {
    static Logger log = LoggerFactory.getLogger(Atm.class);

    protected AtmState state = AtmState.READY;

    protected final AtmState initialState;

    private Map<Banknote, AtmBank> cassette = new HashMap<>();
    private Card card;
    private Bank bank;
    private Map<Banknote, Integer> cash;

    public Atm(AtmState initialState, Bank bank) {
        this.initialState = initialState;
        this.bank = bank;

        init();
    }

    public void addCassette(Banknote banknote, int banknoteCapacity) {
        cassette.put(banknote, new AtmBank(banknoteCapacity));
    }

    public void insertCard(Card card, String pin) {
        state.insertCard(this, () -> {
            boolean result = card.checkPin(pin);

            if (result) {
                this.card = card;
            }

            return result;
        });
    }

    public void getCardBack() {
        state.getCardBack(this, () -> {
            card = null;

            return true;
        });
    }

    public Money getBalanceAccount() {
        return state.getBalance(() -> {
            Account account = bank.getAccountByCard(card);
            Money balance = account.getBalance();

            log.info(String.format("Account balance - %s", balance.toString()));

            return balance;
        });
    }

    public List<Money> getBalanceAtm() {
        List<Money> bundle = new ArrayList<>();

        Map<ru.otus.java_2018_08.student.hw08.common.Currency, Integer> map = new HashMap<>();

        cassette.forEach((b, a) -> {
            Money money = new Money(b.getCurrency(),b.getNominal() * a.getCount());

            int prev = bundle.indexOf(money);

            if (prev < 0) {
                bundle.add(money);
            }
            else {
                bundle.get(prev).addMoney(money);
            }
        });

        log.info(String.format("Total in ATM - %s", bundle.toString()));

        return bundle;
    }

    public boolean cashOut(final int sum, ru.otus.java_2018_08.student.hw08.common.Currency currency) {
        boolean result = state.tryPullBanknote(this, () -> {
            Money balance = getBalanceAccount();

            if (currency == balance.getCurrency() && balance.getAmount() < sum) {
                log.warn("Low balance");

                return false;
            }

            cash = calculateCash(sum, currency, this::checkCountCashToOut);
            if (cash == null) {
                log.warn("Not enough cash");

                return false;
            }

            return true;
        });

        result = result && state.pullBanknote(this, () -> {
            bank.getAccountByCard(card).debit(sum);
            cash.forEach((b, i) ->
                cassette.get(b).pull(i)
            );

            log.info("Cash out: {}", cash.toString());
            cash = null;

            return true;
        });

        log.info("Current cash in ATM {}", cassette.toString());

        return result;
    }

    public void addCash() {
        cassette.forEach((b, ab) -> {
            ab.put(ab.getFreeSpace());
        });
    }

    public boolean cashIn(List<Banknote> moneyBundle) {
        Money money = Money.createByBundle(moneyBundle);

        boolean result = state.tryPutBanknote(this, () -> {
            cash = decomposeBundle(moneyBundle);

            if (!checkSpace(cash)) {
                log.warn("Not enough space");

                return false;
            }

            return true;
        });

        result = result && state.putBanknote(this, () -> {
            cash.forEach((b, i) ->
                cassette.get(b).put(i)
            );
            bank.getAccountByCard(card).deposit(money.getAmount());

            log.info("Cash in: {}", cash.toString());
            cash = null;

            return true;
        });

        log.info("Current cash in ATM {}", cassette.toString());

        return result;
    }

    public boolean reset() {
        return state.reset(this, () -> {
            if (card != null) {
                getCardBack();
            }

            return true;
        });
    }

    protected abstract void init();

    private Map<Banknote, Integer> decomposeBundle(List<Banknote> bundle) {
        Map<Banknote, Integer> mapCash = new HashMap<>();

        for (Banknote banknote : bundle) {
            Integer count = 1;

            if (!mapCash.containsKey(banknote)) {
                mapCash.put(banknote, count);
            }
            else {
                count = mapCash.get(banknote);
                mapCash.put(banknote, count + 1);
            }
        }

        return mapCash;
    }

    private boolean checkSpace(Map<Banknote, Integer> cash) {
        for (Map.Entry<Banknote, Integer> item : cash.entrySet()) {
            AtmBank atmBank = cassette.get(item.getKey());

            if (atmBank == null || atmBank.getFreeSpace() < item.getValue()) {
                return false;
            }
        }

        return true;
    }

    private Map<Banknote, Integer> calculateCash(final int sum, Currency currency, BiFunction<AtmBank, Integer, Boolean> getRemainder) {
        Set<Banknote> set = cassette.keySet().stream().filter(
                b -> b.getCurrency() == currency
        ).sorted(
                Comparator.comparingInt(Banknote::getNominal).reversed()
        ).collect(Collectors.toCollection(LinkedHashSet::new));

        int localSum = sum;
        Map<Banknote, Integer> mapCash = new HashMap<>();
        for (Banknote b : set) {
            int nominal = b.getNominal();
            AtmBank atmBank = cassette.get(b);
            Integer banknoteCount = 0;

            while (localSum >= nominal && getRemainder.apply(atmBank, banknoteCount)) {
                if (localSum >= nominal) {
                    localSum -= nominal;
                    banknoteCount++;
                }
            }

            if (banknoteCount > 0) {
                mapCash.put(b, banknoteCount);
            }
        }

        if (localSum != 0) {
            return null;
        }

        return mapCash;
    }

    private Boolean checkCountCashToOut(AtmBank atmBank, int banknoteCount) {
        return (atmBank.getCount() - banknoteCount) > 0;
    }

    private Boolean checkCountCashToIn(AtmBank atmBank, int banknoteCount) {
        return (atmBank.getFreeSpace() + banknoteCount) > 0;
    }
}
