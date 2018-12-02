package ru.otus.java_2018_08.student.hw08.main;

import ru.otus.java_2018_08.student.hw08.bank.Bank;
import ru.otus.java_2018_08.student.hw08.bank.acquire.Atm;
import ru.otus.java_2018_08.student.hw08.bank.acquire.AtmState;
import ru.otus.java_2018_08.student.hw08.common.Banknote;

public class GenericAtm extends Atm {
    public GenericAtm(AtmState initialState, Bank bank) {
        super(initialState, bank);
    }

    @Override
    protected void init() {
        addCassette(Banknote.RUB100, 50);
        addCassette(Banknote.RUB50, 50);
    }
}
