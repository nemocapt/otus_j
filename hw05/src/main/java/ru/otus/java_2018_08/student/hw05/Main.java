package ru.otus.java_2018_08.student.hw05;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static private int incBy = 10000000;

    public static void main(String[] args) {
        measure();

        List<Object[]> array = new ArrayList<>();
        while (true) {
            array.add(new Object[incBy]);
            array.set(array.size()-1, new Object[incBy]);

            delay(1000);
        }
    }

    protected static void measure() {
        GCInfo.gcListener();

        new Thread( () -> {
            while (true) {
                GCInfo.printGcInfo();
                delay(1000);
            }
        }).start();
    }

    protected static void delay(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
