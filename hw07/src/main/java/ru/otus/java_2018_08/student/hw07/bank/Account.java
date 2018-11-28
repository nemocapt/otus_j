package ru.otus.java_2018_08.student.hw07.bank;

import ru.otus.java_2018_08.student.hw07.common.Currency;
import ru.otus.java_2018_08.student.hw07.common.Money;

import java.util.Objects;
import java.util.UUID;

public class Account {
    private UUID uuid = UUID.randomUUID();
    private String name;
    private int balance = 0;
    private Currency currency;

    protected Account(String name, Currency currency) {
        setName(name);
        setCurrency(currency);
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void debit(int amount) {
        balance -= amount;
    }

    public Money getBalance() {
        return new Money(currency, balance);
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setCurrency(Currency currency) {
        this.currency = currency;
    }

    protected String getName() {
        return name;
    }

    protected UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) &&
                Objects.equals(uuid, account.uuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, uuid);
    }

    @Override
    public String toString() {
        return String.format("%d %s", balance, currency.getCurrencyView());
    }
}
