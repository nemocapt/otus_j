package ru.otus.java_2018_08.student.hw10.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw10.morm.Requester;
import ru.otus.java_2018_08.student.hw10.morm.UserDataSet;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);

    static public void main(String[] args) {
        Requester requester = new Requester("jdbc:mariadb://localhost:3306/users_test", "test", "test");

        UserDataSet user = new UserDataSet();
        user.age = 42;
        user.name = "Name";

        requester.save(user);
        System.out.println(user);

        UserDataSet user2 = requester.load(user.getId(), UserDataSet.class);

        System.out.println(user2.toString());

    }
}
