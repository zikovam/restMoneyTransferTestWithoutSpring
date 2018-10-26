package com.zikovam.utils;

import com.zikovam.entity.Account;
import com.zikovam.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static final ThreadLocal<Session> threadLocal;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Account.class);

            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            threadLocal = new ThreadLocal<>();

        } catch (Throwable t) {
            t.printStackTrace();
            throw new ExceptionInInitializerError(t);
        }
    }

    public static Session getSession () {
        Session session = threadLocal.get();
        if (session == null) {
            session = sessionFactory.openSession();
            threadLocal.set(session);
        }
        return session;
    }

    public static void closeSession () {
        Session session = threadLocal.get();
        if (session != null) {
            session.close();
            threadLocal.set(null);
        }
    }

    public static void closeSessionFactory () {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

}

