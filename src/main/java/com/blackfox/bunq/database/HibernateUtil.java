package com.blackfox.bunq.database;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Session;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(ClientAuth.class);
            configuration.addAnnotatedClass(Transaction.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed.\n" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
        getSessionFactory().close();
    }

    public static ClientAuth getCientAuth(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery("WHERE username = :username",
                            ClientAuth.class)
                    .setParameter("username", username);

            if (query.list().size() != 1) {
                return null;
            }

            ClientAuth client = query.list().getFirst();
            session.close();
            return client;
        }
    }

    public static Client getCient(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery("WHERE username = :username",
                            Client.class)
                    .setParameter("username", username);

            if (query.list().size() != 1) {
                return null;
            }

            Client client = query.list().getFirst();
            session.close();
            return client;
        }
    }

    public static List<Transaction> getTransactionList(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery("WHERE sender = :username OR reveiver = :username",
                            Transaction.class)
                    .setParameter("username", username);

            if (query.list().size() == 0) {
                return null;
            }

            session.close();
            return query.list();
        }
    }
}
