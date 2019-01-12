package ru.otus.java_2018_08.student.hw11.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw11.morm.AddressDataSet;
import ru.otus.java_2018_08.student.hw11.morm.PhoneDataSet;
import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;
import ru.otus.java_2018_08.student.hw11.service.DBService;
import ru.otus.java_2018_08.student.hw11.service.DBServiceHibernateImpl;
import ru.otus.java_2018_08.student.hw11.service.DBServiceImpl;

import java.util.Collections;
import java.util.List;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);

    static public void main(String[] args) {
        /*
         * Hibernate реализация
         */

        DBService serviceHibernate = new DBServiceHibernateImpl("maria_jpa");
        operations(serviceHibernate);
        serviceHibernate.shutdown();

        /*
         * Собственная реализация
         */

        DBService service = new DBServiceImpl("jdbc:mariadb://localhost:3306/users_test", "test", "test");
        operations(service);
        service.shutdown();
    }

    static private void operations(DBService service) {
        UserDataSet user = new UserDataSet(
                "Name",
                42,
                new AddressDataSet("Lenina"),
                Collections.singletonList(new PhoneDataSet("8888"))
        );
        service.save(user);
        System.out.println("--- Insert new user");
        System.out.println(user);

        UserDataSet user2 = service.read(user.getId());
        System.out.println("--- Fetch user by ID");
        System.out.println(user2);

        UserDataSet user3 = service.readByName(user.getName());
        System.out.println("--- Fetch user by name");
        System.out.println(user3);

        UserDataSet user4 = new UserDataSet(
                "Eman",
                24,
                new AddressDataSet("Sovetskaya"),
                Collections.singletonList(new PhoneDataSet("9999"))
        );
        service.save(user4);
        List<UserDataSet> list = service.readAll();
        System.out.println("--- Fetch users");
        for (UserDataSet u : list) {
            System.out.println(u);
        }
    }
}
