package ru.otus.java_2018_08.student.hw05;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Код из примера лекции
public class GCInfo {
    static final Set<String> YOUNG_GC = new HashSet<String>(3);
    static final Set<String> OLD_GC = new HashSet<String>(3);

    static {
        // young generation GC names
        YOUNG_GC.add("PS Scavenge");
        YOUNG_GC.add("ParNew");
        YOUNG_GC.add("G1 Young Generation");

        // old generation GC names
        OLD_GC.add("PS MarkSweep");
        OLD_GC.add("ConcurrentMarkSweep");
        OLD_GC.add("G1 Old Generation");
    }

    static private long minorCount = 0;
    static private long minorTime = 0;
    static private long majorCount = 0;
    static private long majorTime = 0;
    static private long unknownCount = 0;
    static private long unknownTime = 0;

    static public void printInfo() {

        List<GarbageCollectorMXBean> mxBeans
                = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : mxBeans) {
            long count = gc.getCollectionCount();
            if (count >= 0) {
                if (YOUNG_GC.contains(gc.getName())) {
                    minorCount += count;
                    minorTime += gc.getCollectionTime();
                } else if (OLD_GC.contains(gc.getName())) {
                    majorCount += count;
                    majorTime += gc.getCollectionTime();
                } else {
                    unknownCount += count;
                    unknownTime += gc.getCollectionTime();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("MinorGC -> Count: ").append(minorCount)
                .append(", Time (ms): ").append(minorTime)
                .append(", MajorGC -> Count: ").append(majorCount)
                .append(", Time (ms): ").append(majorTime);

        if (unknownCount > 0) {
            sb.append(", UnknownGC -> Count: ").append(unknownCount)
                    .append(", Time (ms): ").append(unknownTime);
        }

        System.out.println(sb);
    }

    static public void clear() {
        minorCount = 0;
        minorTime = 0;
        majorCount = 0;
        majorTime = 0;
        unknownCount = 0;
        unknownTime = 0;
    }

}
