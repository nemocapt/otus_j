package ru.otus.java_2018_08.student.hw06.engine;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.time.Instant;
import java.util.function.Consumer;

class CacheElement<V> extends SoftReference<V> {
    private Instant creationTime;
    private Instant lastAccessTime;

    public CacheElement(V value) {
        super(value);

        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    private Instant getCurrentTime() {
        return Instant.now();
    }

    private long convertTime(Instant instant) {
        return instant.getEpochSecond();
    }

    public V getValue() {
        lastAccessTime = getCurrentTime();
        return get();
    }

    public long getCreationTime() {
        return convertTime(creationTime);
    }

    public Instant getCreationTimeInstant() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return convertTime(lastAccessTime);
    }

    public Instant getLastAccessTimeInstant() {
        return lastAccessTime;
    }
}
