package ru.otus.java_2018_08.student.hw13.main;

import java.util.Arrays;

public class Main {
    static public void main(String[] args) {
        int[] array = {29, 58, 9, 0, 2, 5, 52, 66, 1, 32, 9};
        MultiQSort sort = new MultiQSort(array);
        sort.sort();

        System.out.println(Arrays.toString(array));
    }
}
