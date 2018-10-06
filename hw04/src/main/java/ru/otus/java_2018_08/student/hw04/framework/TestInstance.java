package ru.otus.java_2018_08.student.hw04.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.otus.java_2018_08.student.hw04.framework.TestUtility.error;
import static ru.otus.java_2018_08.student.hw04.framework.TestUtility.info;

public class TestInstance {
    static final private Logger log = Logger.getLogger(TestInstance.class.getName());

    private Class<?> testClass;
    private List<Method> testMethods;
    private List<Boolean> testResults;
    private Method beforeMethod;
    private Method afterMethod;

    private Object testObject;
    private boolean tested = false;

    protected TestInstance(Class<?> clazz) {
        testClass = clazz;
    }

    public boolean doTest() {
        tested = false;

        if (checkClazz() && fillMethods() && doInstance()) {
            runTestMethods();
        }

        return tested;
    }

    public Map<String, Boolean> checkResults() {
        if (!tested) {
            return null;
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        for (int i = 0; i < testMethods.size(); i++) {
            map.put(testMethods.get(i).getName(), testResults.get(i));
        }

        return map;
    }

    protected Class<?> getTestClass() {
        return testClass;
    }

    private boolean doInstance() {
        try {
            testObject = testClass.newInstance();
        }
        catch (Exception e) {
            error(log, "Не удается создать объект класса '%s'", testClass.getCanonicalName());

            return false;
        }

        return true;
    }

    private boolean checkClazz() {
        if  (!testClass.isAnnotationPresent(TestClass.class))
            return false;

        info(log, "Найден тест класс '%s'", testClass.getCanonicalName());

        return true;
    }

    private List<Method> checkMethods(Class<? extends Annotation> annotation) {
        return Arrays.stream(testClass.getMethods())
                .filter(
                        m -> m.isAnnotationPresent(annotation)
                )
                .collect(
                        Collectors.toList()
                );
    }

    private boolean fillMethods() {
        testMethods = checkMethods(Test.class);

        if (testMethods.isEmpty()) {
            info(log, "Класс %s не содержит тестовых методов", testClass.getCanonicalName());

            return false;
        }

        beforeMethod = checkMethods(Before.class).stream().findFirst().orElse(null);
        afterMethod = checkMethods(After.class).stream().findFirst().orElse(null);

        return true;
    }

    private void runTestMethods() {
        testResults = new ArrayList<>();

        for (Method method : testMethods) {
            testResults.add(runTestMethod(method));
        }

        tested = true;
    }

    private boolean runTestMethod(Method method) {
        boolean success = true;
        info(log, "--> Запуск теста '%s'", method.getName());

        try {
            if (beforeMethod != null) {
                info(log, "Выполнения предварительной обработки");

                beforeMethod.invoke(testObject);
            }
        }
        catch (Exception e) {
            error(log, "Не удалось выполнить предварительную обработку");
            error(log, e.getMessage());

            success = false;
        }

        success = success && invokeTest(method);

        try {
            if (afterMethod != null) {
                info(log, "Выполнение заключительной обработки");

                afterMethod.invoke(testObject);
            }
        }
        catch (Exception e) {
            error(log, "Не удалось выполнить заключительную обработку");
            error(log, e.getMessage());

            success = false;
        }

        if (success) {
            info(log, "<-- Тест успешно выполнен");
        }
        else {
            info(log, "<-- Тест не пройден");
        }

        return success;
    }

    private boolean invokeTest(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        String[] params = testAnnotation.params();

        try {
            info(log, "Выполнение теста");
            method.invoke(testObject, params);
        }
        catch (Exception e) {
            error(log, "Не удалось выполнить тест");
            error(log, e.getCause().getMessage());

            return false;
        }

        return true;
    }
}
