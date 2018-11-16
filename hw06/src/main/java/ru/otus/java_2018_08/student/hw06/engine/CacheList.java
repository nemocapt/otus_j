package ru.otus.java_2018_08.student.hw06.engine;

import java.time.Instant;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheList<K, V> {

    private Hashtable<K, CacheElement<V>> map = new Hashtable<>();
    private int hit = 0;
    private int miss = 0;
    private long timeLife = 10; // seconds
    private long timeShedulePeriod = 2; // seconds

    private ScheduledExecutorService shedule = Executors.newScheduledThreadPool(1);

    public CacheList() {
        doTask();
    }

    public CacheList(long timeLife) {
        this();

        this.timeLife = timeLife;
        this.timeShedulePeriod = timeLife;
    }

    private void delete() {
        Instant old = Instant.now().minusSeconds(timeLife);

        int oldCount = 0;
        int nullCount = 0;
        Enumeration<K> keys = map.keys();

        while (keys.hasMoreElements()) {
            K key = keys.nextElement();
            CacheElement<V> val = map.get(key);
            if (val.get() == null) {
                nullCount++;
                map.remove(key);
            }
            if (old.isAfter(val.getLastAccessTimeInstant())) {
                oldCount++;
                map.remove(key);
            }
        }

        System.out.println(String.format("\t<+> removed: {oldCount - %d; nullCount - %d}", oldCount, nullCount));
        System.out.println(String.format("\t<+> size: %d", map.size()));
    }

    private void doTask() {
        shedule.scheduleAtFixedRate(this::delete, timeShedulePeriod, timeShedulePeriod, TimeUnit.SECONDS);
    }

    public void put(K key, V val) {
        CacheElement<V> element = new CacheElement<>(val);

        map.put(key, element);
    }

    public V get(K key) {
        CacheElement<V> val = map.getOrDefault(key, null);

        if (val == null) {
            miss++;

            return null;
        } else {
            hit++;
        }

        return val.getValue();
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }
    public void close() {
        shedule.shutdown();
    }

    public int size() {
        return map.size();
    }
}
