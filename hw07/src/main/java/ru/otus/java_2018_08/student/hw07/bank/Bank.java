package ru.otus.java_2018_08.student.hw07.bank;

import ru.otus.java_2018_08.student.hw07.bank.acquire.Card;
import ru.otus.java_2018_08.student.hw07.common.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bank {
    private int panCount = 0;
    private String defaultPin = "0000";
    private List<Account> accounts = new ArrayList<>();
    private Map<String, Account> cards = new HashMap<>();

    private Account findAccountByName(String name) {
        return accounts.stream().filter(account ->
            account.getName().equals(name)
        ).findFirst().orElse(null);
    }

    public Account openAccount(String name, Currency currency) {
        Account account = new Account(name, currency);
        accounts.add(account);

        return account;
    }

    public Card openCard(String accountName, Currency currency) {
        Account account = findAccountByName(accountName);
        if (account == null) {
            account = openAccount(accountName, currency);
        }

        return bindCard(account, defaultPin);
    }

    public Account getAccountByCard(Card card) {
        return cards.get(card.getPan());
    }

    private Card bindCard(Account account, String pin) {
        Card card = new Card(generatePan(), pin);
        cards.put(card.getPan(), account);

        return card;
    }

    private String generatePan() {
        return String.format("%016d", panCount);
    }
}
