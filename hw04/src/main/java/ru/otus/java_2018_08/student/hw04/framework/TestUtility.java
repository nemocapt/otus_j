package ru.otus.java_2018_08.student.hw04.framework;

import java.util.logging.Logger;

public class TestUtility {
    private TestUtility() {

    }

    static public void info(Logger log, String line, String... params) {
        log.info(
                String.format("(---) " + line, params)
        );
    }

    static public void error(Logger log, String line, String... params) {
        log.severe(
                String.format("(!!!) " + line, params)
        );
    }
}
