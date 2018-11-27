package ru.otus.java_2018_08.student.hw07.common;

public enum Banknote {
    RUB100(100, Currency.RUB),
    RUB50(50, Currency.RUB);

    private int nominal;
    private Currency currency;

    Banknote(int nominal, Currency currency) {
        this.nominal = nominal;
        this.currency = currency;
    }

    public int getNominal() {
        return nominal;
    }

    public Currency getCurrency() {
        return currency;
    }
}
