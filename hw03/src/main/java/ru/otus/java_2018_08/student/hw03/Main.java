package ru.otus.java_2018_08.student.hw03;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>();

        list.add(10);
        List<Integer> intList = Arrays.asList(new Integer[] {13, 42, 0});
        list.addAll(intList);
        list.add(1, 55);
        list.add(99);
        // 10, 55, 13, 42, 0, 99
        printCollection(list, "Adding");

        list.removeAll(intList);
        list.remove(0);
        list.remove(new Integer(99));
        // 55
        printCollection(list, "Removing");

        list.addAll(intList);
        // 55, 13, 42, 0
        printCollection(list, "Before sorting");
        Collections.sort(list, Integer::compareTo);
        // 0, 13, 42, 55
        printCollection(list, "Collections.sort()");

        Integer intMax = Collections.max(list, Integer::compareTo);
        // 55
        printObject(intMax, "Collections.max()");

        Collections.rotate(list, 2);
        // 42, 55, 0, 13
        printCollection(list, "Collections.rotate()");

        Collections.shuffle(list);
        printCollection(list, "Collections.shuffle()");
    }

    public static void printCollection(Collection<?> collection, String name) {
        System.out.println(name);
        System.out.println(String.format("size: %d", collection.size()));
        for (Object o : collection) {
            System.out.println(String.format("value: %s", o.toString()));
        }
    }

    public static void printObject(Object obj, String name) {
        System.out.println(name);
        System.out.println(String.format("object: %s", obj.toString()));
    }
}
