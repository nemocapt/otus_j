package ru.otus.java_2018_08.student.hw07.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw07.bank.Account;
import ru.otus.java_2018_08.student.hw07.bank.Bank;
import ru.otus.java_2018_08.student.hw07.common.Banknote;
import ru.otus.java_2018_08.student.hw07.common.Currency;
import ru.otus.java_2018_08.student.hw07.common.Money;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class Atm {
    static Logger log = LoggerFactory.getLogger(Atm.class);

    protected AtmState state = AtmState.READY;

    private Map<Banknote, AtmBank> cassette = new HashMap<>();
    private Card card;
    private Bank bank;
    private Map<Banknote, Integer> cash;

    public Atm(Bank bank) {
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

            log.info(balance.toString());

            return balance;
        });
    }

    public List<Money> getBalanceAtm() {
        List<Money> bundle = new ArrayList<>();

        Map<Currency, Integer> map = new HashMap<>();

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

    public boolean cashOut(final int sum, Currency currency) {
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

    public boolean cashIn(List<Banknote> moneyBundle) {
        Money money = Money.createByBundle(moneyBundle);

        boolean result = state.tryPutBanknote(this, () -> {
            if (money == null) {
                log.warn("Different banknotes");

                return false;
            }

            cash = calculateCash(money.getAmount(), money.getCurrency(), this::checkCountCashToIn);
            if (cash == null) {
                log.warn("Not enough space");

                return false;
            }

            return true;
        });

        result = result && state.putBanknote(this, () -> {
            moneyBundle.forEach(b ->
                cassette.get(b).put(1)
            );
            bank.getAccountByCard(card).deposit(money.getAmount());

            log.info("Cash in: {}", cash.toString());
            cash = null;

            return true;
        });

        log.info("Current cash in ATM {}", cassette.toString());

        return result;
    }

    protected abstract void init();

    private Map<Banknote, Integer> decomposeBundle(List<Banknote> bundle) {
        Map<Banknote, Integer> mapCash = new HashMap<>();

        for (Banknote banknote : bundle) {
            Integer count = new Integer(1);

            if (!mapCash.containsKey(banknote)) {
                mapCash.put(banknote, count);
            }
            else {
                count = mapCash.get(banknote);
                count++;
            }
        }

        return mapCash;
    }

    private Map<Banknote, Integer> calculateCash(final int sum, Currency currency, BiFunction<AtmBank, Integer, Boolean> getRemainder) {
        Set<Banknote> set = cassette.keySet().stream().filter(
                b -> b.getCurrency() == currency
        ).sorted(
                Comparator.comparingInt(Banknote::getNominal).reversed()
        ).collect(Collectors.toSet());

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
