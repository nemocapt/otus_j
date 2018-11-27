package ru.otus.java_2018_08.student.hw07.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw07.bank.Bank;
import ru.otus.java_2018_08.student.hw07.bank.acquire.Atm;
import ru.otus.java_2018_08.student.hw07.bank.acquire.Card;
import ru.otus.java_2018_08.student.hw07.common.Currency;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);
    static public void main(String[] args) {
        log.info("--- Создаем счет");
        Bank bank = new Bank();
        Card card = bank.openCard("test_account", Currency.RUB);

        log.info("--- Получаем банкомат");
        Atm atm = new GenericAtm(bank);

        log.info("=== Вставляем карту в банкомат, неверный ПИН");
        atm.insertCard(card, "000");

        log.info("=== Вставляем карту в банкомат");
        atm.insertCard(card, "0000");

        log.info("=== Проверяем счет");
        atm.getBalance();

        log.info("=== Вносим 5050");
        atm.cashIn(5050, Currency.RUB);
        atm.getBalance();

        log.info("=== Пытаемся снять некорректную сумму - 240");
        atm.cashOut(240, Currency.RUB);
        atm.getBalance();

        log.info("=== Пытаемся снять больше остатка на карте");
        atm.cashOut(5100, Currency.RUB);
        atm.getBalance();

        log.info("=== Снимаем 250");
        atm.cashOut(250, Currency.RUB);
        atm.getBalance();

        log.info("=== Изымаем карту");
        atm.getCardBack();
    }
}
