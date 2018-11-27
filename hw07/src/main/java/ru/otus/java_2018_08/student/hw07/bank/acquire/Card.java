package ru.otus.java_2018_08.student.hw07.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Card {
    static private Logger log = LoggerFactory.getLogger(Card.class);

    private String pan;
    private String pin;

    public Card(String pan, String pin) {
        this.pan = pan;
        this.pin = pin;
    }

    public String getPan() {
        return pan;
    }

    public boolean checkPin(String pin) {
        boolean result = this.pin.equals(pin);

        if (!result) {
            log.warn(String.format(
                    "Wrong PAN for PAN \"%s\"", pan
            ));
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(pan, card.pan);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pan);
    }
}
