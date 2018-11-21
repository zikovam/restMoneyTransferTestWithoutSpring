package com.zikovam.dao;

import com.zikovam.entity.Account;
import com.zikovam.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccountDaoImpl implements AccountDao {
    @Override
    public Account getAccountById (long id) {
        Session session = HibernateSessionFactoryUtil.getSession();
        return session.get(Account.class, id);
    }

    @Override
    public void createAccount (Account account) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(account);
        tx1.commit();
    }

    @Override
    public void deleteAccount (Account account) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(account);
        tx1.commit();
    }

    @Override
    public void updateAccount (Account account) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(account);
        tx1.commit();
    }
}
