package ru.otus.java_2018_08.student.hw08.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw08.bank.Bank;
import ru.otus.java_2018_08.student.hw08.bank.acquire.Atm;
import ru.otus.java_2018_08.student.hw08.bank.acquire.AtmDepartment;
import ru.otus.java_2018_08.student.hw08.bank.acquire.AtmState;
import ru.otus.java_2018_08.student.hw08.bank.acquire.Card;
import ru.otus.java_2018_08.student.hw08.common.Currency;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);
    static public void main(String[] args) throws InterruptedException {
        log.info("--- Создаем счет");
        Bank bank = new Bank();
        Card card = bank.openCard("test_account", Currency.RUB);

        log.info("--- Добавляем банкоматы в департамент");
        AtmDepartment department = new AtmDepartment();

        Atm genericAtm = new GenericAtm(AtmState.READY, bank);
        genericAtm.addCash();
        department.addAtm(genericAtm);

        Atm otherAtm = new OtherAtm(AtmState.OFF, bank);
        otherAtm.addCash();
        department.addAtm(otherAtm);

        log.info("--- Балансы банкоматов");
        log.info(department.getBalances().toString());

        genericAtm.insertCard(card, "0000");
        otherAtm.insertCard(card, "0000");

        log.info("--- Сброс банкоматов");
        log.info(department.resetAtm().toString());

        department.close();
    }
}
