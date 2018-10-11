package ru.otus.java_2018_08.student.hw05;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
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

    static public void clear() {
        minorCount = 0;
        minorTime = 0;
        majorCount = 0;
        majorTime = 0;
        unknownCount = 0;
        unknownTime = 0;
    }

    static public void gcListener() {
        List<GarbageCollectorMXBean> gc = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcItem : gc) {
            NotificationEmitter emitter = (NotificationEmitter) gcItem;

            NotificationListener listener = (notification, handback) -> {
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }

                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                if (YOUNG_GC.contains(info.getGcName())) {
                    minorCount++;
                    minorTime += info.getGcInfo().getDuration();
                } else if (OLD_GC.contains(info.getGcName())) {
                    majorCount++;
                    majorTime += info.getGcInfo().getDuration();
                } else {
                    unknownCount++;
                    unknownTime += info.getGcInfo().getDuration();
                }
            };

            emitter.addNotificationListener(listener, null, null);
        }
    }

    static public void printGcInfo() {
        printThisGc("MinorGC", minorCount, minorTime);
        printThisGc("MajorGC", majorCount, majorTime);
        printThisGc("UnknownGC", unknownCount, unknownTime);
        System.out.println("--------------------------------");
    }

    static private void printThisGc(String gcName, long count, long time) {
        StringBuilder sb = new StringBuilder();

        sb.append(gcName);

        if (count > 0) {
            sb.append(" -> Count: ").append(count)
                    .append(", Time_last (ms): ").append(time)
                    .append(", Time_mid (ms): ").append(time/count)
                    .append("; ");
        }
        else {
            sb.append(" ; ");
        }

        System.out.println(sb);
    }
}
