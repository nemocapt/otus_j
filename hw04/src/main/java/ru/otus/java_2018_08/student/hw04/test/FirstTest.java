package ru.otus.java_2018_08.student.hw04.test;

import ru.otus.java_2018_08.student.hw04.framework.After;
import ru.otus.java_2018_08.student.hw04.framework.Before;
import ru.otus.java_2018_08.student.hw04.framework.Test;
import ru.otus.java_2018_08.student.hw04.framework.TestClass;

import java.util.logging.Logger;

import static ru.otus.java_2018_08.student.hw04.framework.TestUtility.info;

@TestClass
public class FirstTest {
    static final private Logger log = Logger.getLogger(FirstTest.class.getName());

    @Before
    public void before() {
        info(log, "=== Некоторая подготовка");
    }

    @After
    public void after() {
        info(log, "=== Некоторые завершающие этапы");
    }

    @Test
    public void goodTest() {
        info(log, "=== Тест 1 ===");
        assert true;
    }

    @Test
    public void badTest() {
        info(log, "=== Тест 2 ===");
        assert false : "Что-то не так с тестом";
    }
}
