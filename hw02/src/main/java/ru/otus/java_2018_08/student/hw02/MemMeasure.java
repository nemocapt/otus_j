package ru.otus.java_2018_08.student.hw02;

public class MemMeasure {
    private int count = 10000000;

    static public long getUsedMemory() {
        System.gc();
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public void alloc(Func func) {
        Object[] objs = new Object[count];

        long memBefore = MemMeasure.getUsedMemory();

        for (int i = 0; i < objs.length; i++) {
            objs[i] = func.alloc();
        }

        long memAfter = MemMeasure.getUsedMemory();
        long memUnit = (memAfter - memBefore) / count;
        System.out.println(
                String.format("Mem for \"%s\": %d bytes", objs[0].getClass().getCanonicalName(), memUnit)
        );
    }

    @FunctionalInterface
    interface Func {
        Object alloc();
    }
}
