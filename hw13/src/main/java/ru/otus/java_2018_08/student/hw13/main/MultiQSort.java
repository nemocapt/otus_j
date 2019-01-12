package ru.otus.java_2018_08.student.hw13.main;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiQSort {
    final private int MAX_SIZE = 4;

    private AtomicInteger threads;

    private int[] array;

    public MultiQSort(int[] array) {
        this.array = array;
        threads = new AtomicInteger(MAX_SIZE);
    }

    public void sort() {
        threads.set(MAX_SIZE);
        multiSort(0, array.length - 1);
    }

    private void sort(int left, int right) {
            if (left < right) {
                int pivot = partSort(left, right);
                sort(left, pivot);
                sort(pivot + 1, right);
            }
    }

    private void multiSort(int left, int right) {
        if (threads.get() > 0) {

            System.out.println(Thread.currentThread().getName());

            if (left < right) {
                int pivot = partSort(left, right);

                Thread tLeft = new Thread(() -> multiSort(left, pivot));
                threads.decrementAndGet();
                Thread tRight = new Thread(() -> multiSort(pivot + 1, right));
                threads.decrementAndGet();

                tLeft.start();
                tRight.start();

                try {
                    tLeft.join();
                    tRight.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            sort(left, right);
        }
    }

    private int partSort(int left, int right) {
            int pivot = array[(left + right) / 2];
            int i = left;
            int j = right;
            while (i < j) {
                while (array[i] < pivot) {
                    i++;
                }
                while (array[j] > pivot) {
                    j--;
                }

                if (i < j) {
                    int k = array[i];
                    array[i++] = array[j];
                    array[j--] = k;
                }
            }

            return j;
    }
}
