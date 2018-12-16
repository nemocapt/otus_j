package ru.otus.java_2018_08.student.hw10.morm;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Requester {
    private Connection connection;

    private final String selectStatement = "SELECT * FROM %s WHERE id = ?";
    private final String insertStatement = "INSERT INTO %s (%s) values (%s)";

    public Requester(String uri, String login, String pass) {
        try {
            connection = DriverManager.getConnection(uri, login, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends DataSet> void save(T user) {
        try {
            fillColumns(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        T result = null;
        try {
            result = clazz.newInstance();

            PreparedStatement statement = connection.prepareStatement(
                    String.format(selectStatement, result.getTableName())
            );
            statement.setLong(1, id);

            result = fillFields(clazz, statement.executeQuery());
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return result;
    }

    private <T extends DataSet> T fillFields(Class<T> clazz, ResultSet resultSet) throws SQLException, IllegalAccessException {
        T result = null;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        List<Field> fields = Arrays.asList(clazz.getFields());
        if (resultSet.next()) {
            result.setId((long) resultSet.getObject("id"));

            for (Field field : fields) {
                field.set(result, resultSet.getObject(field.getName()));
            }
        }

        return result;
    }

    private <T extends DataSet> void fillColumns(T dataSet) throws SQLException {
        List<Field> fields = Arrays.asList(dataSet.getClass().getFields());

        String fieldsName = fields.stream().map(Field::getName).collect(Collectors.joining(", "));

        String fieldsValue = fields.stream().map(f -> {
            try {
                return "'" + f.get(dataSet).toString() + "'";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return "";
        }).collect(Collectors.joining(", "));

        String request = String.format(insertStatement, dataSet.getTableName(), fieldsName, fieldsValue);
        PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
        if (statement.executeUpdate() > 0) {
            ResultSet resultId = statement.getGeneratedKeys();
            if (resultId.next()) {
                dataSet.setId(resultId.getLong(1));
            }
        }
    }
}
