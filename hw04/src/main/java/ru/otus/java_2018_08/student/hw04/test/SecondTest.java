package ru.otus.java_2018_08.student.hw04.test;

import ru.otus.java_2018_08.student.hw04.framework.Test;
import ru.otus.java_2018_08.student.hw04.framework.TestClass;

import java.util.logging.Logger;

import static ru.otus.java_2018_08.student.hw04.framework.TestUtility.info;

@TestClass
public class SecondTest {
    static final private Logger log = Logger.getLogger(SecondTest.class.getName());

    @Test(params = {"param1", "42"})
    public void someTest(String p1, String p2) {
        info(log, "=== Тест ===");
        info(log,"Получили параметры '%s', '%s'", p1, p2);
    }
}
