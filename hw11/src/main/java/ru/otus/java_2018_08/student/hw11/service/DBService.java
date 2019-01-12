package ru.otus.java_2018_08.student.hw11.service;

import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;

import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(UserDataSet dataSet);

    UserDataSet read(long id);

    UserDataSet readByName(String name);

    List<UserDataSet> readAll();

    void shutdown();
}
