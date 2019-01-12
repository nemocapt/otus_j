package ru.otus.java_2018_08.student.hw11.service;

import ru.otus.java_2018_08.student.hw11.dao.UsersHibernateDAO;
import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private EntityManagerFactory emFactory;

    public DBServiceHibernateImpl(String persistenceUnitName) {
        emFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    @Override
    public String getLocalStatus() {
        return runByManager(manager -> manager.getTransaction().toString());
    }

    @Override
    public void save(UserDataSet dataSet) {
        UsersHibernateDAO dao = new UsersHibernateDAO(emFactory.createEntityManager());
        dao.save(dataSet);
    }

    @Override
    public UserDataSet read(long id) {
        return runByManager(manager -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(emFactory.createEntityManager());

            return dao.read(id);
        });
    }

    @Override
    public UserDataSet readByName(String name) {
        return runByManager(manager -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(emFactory.createEntityManager());

            return dao.readByName(name);
        });
    }

    @Override
    public List<UserDataSet> readAll() {
        return runByManager(manager -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(emFactory.createEntityManager());

            return dao.readAll();
        });
    }

    @Override
    public void shutdown() {
        emFactory.close();
    }

    private <R> R runByManager(Function<EntityManager, R> function) {
        EntityManager manager = emFactory.createEntityManager();

        manager.getTransaction().begin();
        R result = function.apply(manager);
        manager.getTransaction().commit();
        manager.close();

        return result;
    }
}
