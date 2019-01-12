package ru.otus.java_2018_08.student.hw12.morm.dao;

import lombok.AllArgsConstructor;
import ru.otus.java_2018_08.student.hw12.morm.UserDataSet;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class UsersHibernateDAO {
    private EntityManager manager;

    public void save(UserDataSet dataSet) {
        manager.getTransaction().begin();
        manager.persist(dataSet);
        manager.getTransaction().commit();
        manager.close();
    }

    public UserDataSet read(long id) {
        return manager.find(UserDataSet.class, id);
    }

    public UserDataSet readByName(String name) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));

        List<UserDataSet> list = manager.createQuery(criteria).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<UserDataSet> readAll() {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);

        return manager.createQuery(criteria).getResultList();
    }
}
