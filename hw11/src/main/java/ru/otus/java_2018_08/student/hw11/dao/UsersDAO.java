package ru.otus.java_2018_08.student.hw11.dao;

import lombok.AllArgsConstructor;
import ru.otus.java_2018_08.student.hw11.morm.AddressDataSet;
import ru.otus.java_2018_08.student.hw11.morm.PhoneDataSet;
import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class UsersDAO {
    private Connection connection;

    public void save(UserDataSet dataSet) throws SQLException {
        String requestAddress = "INSERT INTO address (street) VALUES (?)";
        String requestUser = "INSERT INTO user (name, age, address_id) VALUES (?, ?, ?)";
        String requestPhone = "INSERT INTO phone (number, user_id) VALUES (?, ?)";

        PreparedStatement statementAddress = connection.prepareStatement(requestAddress, Statement.RETURN_GENERATED_KEYS);
        statementAddress.setString(1, dataSet.getAddress().getStreet());
        if (statementAddress.executeUpdate() > 0) {
            ResultSet resultId = statementAddress.getGeneratedKeys();
            if (resultId.next()) {
                dataSet.getAddress().setId(resultId.getLong(1));
            }
        }

        PreparedStatement statementUser = connection.prepareStatement(requestUser, Statement.RETURN_GENERATED_KEYS);
        statementUser.setString(1, dataSet.getName());
        statementUser.setInt(2, dataSet.getAge());
        statementUser.setLong(3, dataSet.getAddress().getId());
        if (statementUser.executeUpdate() > 0) {
            ResultSet resultId = statementUser.getGeneratedKeys();
            if (resultId.next()) {
                dataSet.setId(resultId.getLong(1));
            }
        }

        PreparedStatement statementPhone = connection.prepareStatement(requestPhone, Statement.RETURN_GENERATED_KEYS);
        for (PhoneDataSet i : dataSet.getPhoneList()) {
            statementPhone.setString(1, i.getNumber());
            statementPhone.setLong(2, dataSet.getId());
            if (statementPhone.executeUpdate() > 0) {
                ResultSet resultId = statementPhone.getGeneratedKeys();
                if (resultId.next()) {
                    i.setId(resultId.getLong(1));
                }
            }
        }
    }

    public UserDataSet read(long id) throws SQLException {
        String request = "SELECT u.id, name, age, p.id, number, a.id, street FROM user u LEFT JOIN (phone p, address a) ON (u.address_id = a.id and u.id = p.user_id) WHERE u.id = ?";

        PreparedStatement statement = connection.prepareStatement(request);
        statement.setLong(1, id);

        return getByResultSet(statement.executeQuery()).get(0);
    }

    public UserDataSet readByName(String name) throws SQLException {
        String request = "SELECT u.id, name, age, p.id, number, a.id, street FROM user u LEFT JOIN (phone p, address a) ON (u.address_id = a.id and u.id = p.user_id) WHERE name = ?";

        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, name);

        return getByResultSet(statement.executeQuery()).get(0);
    }

    public List<UserDataSet> readAll() throws SQLException {
        String request = "SELECT u.id, name, age, p.id, number, a.id, street FROM user u LEFT JOIN (phone p, address a) ON (u.address_id = a.id and u.id = p.user_id)";

        PreparedStatement statement = connection.prepareStatement(request);

        return getByResultSet(statement.executeQuery());
    }

    private List<UserDataSet> getByResultSet(ResultSet result) throws SQLException {
        List<UserDataSet> users = new LinkedList<>();
        List<PhoneDataSet> phones = null;
        UserDataSet user = null;

        while (result.next()) {
            if (user == null || result.getLong(1) != user.getId()) {
                user = new UserDataSet();
                users.add(user);

                phones = new LinkedList<>();
                user.setPhoneList(phones);

                user.setId(result.getLong(1));
                user.setName(result.getString(2));
                user.setAge(result.getInt(3));

                AddressDataSet address = new AddressDataSet();
                address.setId(result.getLong(6));
                address.setStreet(result.getString(7));
                user.setAddress(address);
            }

            PhoneDataSet phone = new PhoneDataSet();
            phone.setId(result.getLong(4));
            phone.setNumber(result.getString(5));
            phones.add(phone);
        }


        return users;
    }
}
