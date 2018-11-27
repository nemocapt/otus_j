package ru.otus.java_2018_08.student.hw07.common;

public enum Currency {
    RUB("\u20BD", "rub");

    private String view;
    private String description;

    Currency(String view, String description) {
        this.view = view;
        this.description = description;
    }

    public String getCurrencyView() {
        return view;
    }
    public String desctiption() {
        return description;
    }
}
