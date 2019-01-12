package ru.otus.java_2018_08.student.hw11.dao;

import lombok.AllArgsConstructor;
import ru.otus.java_2018_08.student.hw11.morm.UserDataSet;

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

        return manager.createQuery(criteria).getSingleResult();
    }

    public List<UserDataSet> readAll() {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);

        return manager.createQuery(criteria).getResultList();
    }
}
