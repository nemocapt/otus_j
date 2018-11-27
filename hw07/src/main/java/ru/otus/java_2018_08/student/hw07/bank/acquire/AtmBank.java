package ru.otus.java_2018_08.student.hw07.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AtmBank {
    static Logger log = LoggerFactory.getLogger(AtmBank.class);

    private int capacity;
    private int count = 0;

    public AtmBank(int capacity) {
        this.capacity = capacity;
    }

    public boolean put(int count) {
        if (count > getFreeSpace()) {
            log.warn("Not enough slots");

            return false;
        }

        this.count += count;

        return true;
    }

    public boolean pull(int count) {
        if (count > getCount()) {
            log.warn("Not enough banknotes");

            return false;
        }

        this.count -= count;

        return true;
    }

    public int getFreeSpace() {
        return capacity - count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("{capacity: %d; count: %d}", capacity, count);
    }
}
