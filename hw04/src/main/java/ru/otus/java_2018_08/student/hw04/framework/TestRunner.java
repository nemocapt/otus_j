package ru.otus.java_2018_08.student.hw04.framework;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


public class TestRunner {
    static final private Logger log = Logger.getLogger(TestRunner.class.getName());

    static private List<TestInstance> testList = new ArrayList<>();

    static public void runTest(Class<?> clazz) {
        clearResults();

        testList.add(new TestInstance(clazz));

        runAll();
    }

    static public void runTest(String pack) {
        clearResults();

        Reflections reflections = new Reflections(pack);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(TestClass.class);

        classes.forEach(
                c -> testList.add(new TestInstance(c))
        );

        runAll();
    }

    static private void clearResults() {
        testList.clear();
    }

    static private void runAll() {
        testList.forEach(
                TestInstance::doTest
        );
    }
}
