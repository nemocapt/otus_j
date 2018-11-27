package ru.otus.java_2018_08.student.hw07.main;

import ru.otus.java_2018_08.student.hw07.bank.Bank;
import ru.otus.java_2018_08.student.hw07.bank.acquire.Atm;
import ru.otus.java_2018_08.student.hw07.common.Banknote;

public class GenericAtm extends Atm {
    public GenericAtm(Bank bank) {
        super(bank);
    }

    @Override
    protected void init() {
        addCassette(Banknote.RUB100, 50);
        addCassette(Banknote.RUB50, 50);
    }
}
