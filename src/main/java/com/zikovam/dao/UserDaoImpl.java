package com.zikovam.dao;

import com.zikovam.entity.User;
import com.zikovam.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> getAllUsers () {

        Session session = HibernateSessionFactoryUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        CriteriaQuery<User> select = criteriaQuery.select(from);
        TypedQuery<User> typedQuery = session.createQuery(select);

        return typedQuery.getResultList();

    }

    @Override
    public User gerUserById (long id) {
        Session session = HibernateSessionFactoryUtil.getSession();
        return session.get(User.class, id);
    }

    @Override
    public User getUserByUsername (String username) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();

        List<User> users = session.
                createQuery("FROM User U WHERE U.name =:username").
                setParameter("username",username).list();

        tx1.commit();
        return users.get(0);
    }

    @Override
    public void createUser (User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
    }

    @Override
    public void updateUser (User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
    }

    @Override
    public void deleteUser (User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
    }
}
