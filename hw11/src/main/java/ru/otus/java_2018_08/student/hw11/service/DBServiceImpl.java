package ru.otus.java_2018_08.student.hw11.service;

import ru.otus.java_2018_08.student.hw11.dao.UsersDAO;
import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DBServiceImpl implements DBService {
    private Connection connection;

    public DBServiceImpl(String uri, String login, String pass) {
        try {
            connection = DriverManager.getConnection(uri, login, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLocalStatus() {
        String status = "exception";
        try {
            status = connection.isClosed() ? "closed" : "opened";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("status: %s", status);
    }

    @Override
    public void save(UserDataSet dataSet) {
        UsersDAO dao = new UsersDAO(connection);

        try {
            dao.save(dataSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDataSet read(long id) {
        UsersDAO dao = new UsersDAO(connection);

        try {
            return dao.read(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new UserDataSet();
    }

    @Override
    public UserDataSet readByName(String name) {
        UsersDAO dao = new UsersDAO(connection);

        try {
            return dao.readByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new UserDataSet();
    }

    @Override
    public List<UserDataSet> readAll() {
        UsersDAO dao = new UsersDAO(connection);

        try {
            return dao.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new LinkedList<>();
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
