package ru.otus.java_2018_08.student.hw02;

import sun.net.NetworkServer;

import java.util.Random;

public class Main {
    static public void main(String[] args) {
        MemMeasure memMeasure = new MemMeasure();
        Random random = new Random();

        MemMeasure.Func[] testConstructors = {
                () -> new Integer[4],
                () -> new Integer[] {2, 3, 2323, 532},
                () -> new NetworkServer(),
                () -> new String(""),
                () -> new String(String.format("%+020d", random.nextLong())),
                () -> new Object()
        };

        for (MemMeasure.Func f : testConstructors) {
            memMeasure.alloc(f);
        }
    }
}
