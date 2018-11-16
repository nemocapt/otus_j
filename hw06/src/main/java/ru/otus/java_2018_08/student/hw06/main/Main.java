package ru.otus.java_2018_08.student.hw06.main;

import ru.otus.java_2018_08.student.hw06.engine.CacheList;

/*
 * args: -Xmx64m -Xms64m
 */
public class Main {
    static final long timeLife = 10;

    static CacheList<Integer, String> cache = new CacheList<>(timeLife);
    static int requestDelay = 100;

    static public void main(String... args) {
        iteration(100, "Raw requests");
        iteration(100, "Cached requests");
        pause(14000, true);

        requestDelay = 0;
        iteration(200_000, "Many requests");

        cache.close();
    }

    static String doRequest(Integer i) {
        pause(requestDelay, false);

        return i.toString();
    }

    static String get(Integer i) {
        String res = cache.get(i);

        if (res == null) {
            res = doRequest(i);

            cache.put(i, res);
        }

        return res;
    }

    static void iteration(int count, String description) {
        System.out.println(String.format("Starting \"%s\"", description));
        long time = System.currentTimeMillis();
        int sum = 0;

        for (int i = 0; i < count; i++) {
            sum += new Integer(get(i));
        }

        time = System.currentTimeMillis() - time;

        System.out.println(String.format("\"%s\": %d ms, summ = %d", description, time, sum));
        System.out.println(String.format("Misses - %d, hits - %s", cache.getMissCount(), cache.getHitCount()));
        System.out.println("-----");
    }

    static void pause(long delay, boolean print) {
        if (print) {
            System.out.println(String.format("Pause %d ms...", delay));
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
