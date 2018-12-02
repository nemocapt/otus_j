package ru.otus.java_2018_08.student.hw08.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw08.common.Money;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AtmDepartment {
    static private Logger log = LoggerFactory.getLogger(AtmDepartment.class);

    private List<Atm> atmList = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public void addAtm(Atm atm) {
        atmList.add(atm);
    }

    public Map<Atm, List<Money>> getBalances() throws InterruptedException {
        Map<Atm, List<Money>> result = new Hashtable<>();

        for (Atm atm : atmList) {
            executor.execute(() -> {
                result.put(atm, atm.getBalanceAtm());
            });
        }

        executor.awaitTermination(1, TimeUnit.SECONDS);

        return result;
    }

    public Map<Atm, Boolean> resetAtm() throws InterruptedException {
        Map<Atm, Boolean> result = new Hashtable<>();

        for (Atm atm : atmList) {
            executor.execute(() -> {
                result.put(atm, atm.reset());
            });
        }

        executor.awaitTermination(1, TimeUnit.SECONDS);

        return result;
    }

    public void close() {
        executor.shutdownNow();
    }
}
