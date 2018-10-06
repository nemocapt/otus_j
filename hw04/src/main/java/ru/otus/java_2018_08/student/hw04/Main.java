package ru.otus.java_2018_08.student.hw04;

import ru.otus.java_2018_08.student.hw04.framework.TestRunner;
import ru.otus.java_2018_08.student.hw04.test.SecondTest;

public class Main {
    /*
     * Запускаем с VM аргументом -ea
     */

    static public void main(String[] args) {
        // Запуск одного теста
        TestRunner.runTest(SecondTest.class);

        // Запуск пакета тестов
        TestRunner.runTest("ru.otus.java_2018_08.student.hw04.test");
    }
}
