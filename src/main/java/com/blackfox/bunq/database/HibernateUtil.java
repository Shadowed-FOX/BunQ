package com.blackfox.bunq.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import jakarta.persistence.NoResultException;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.Session;

public class HibernateUtil {
    static Client activeClient;
    static ClientAuth activeClientAuth;

    public static void setActiveClientAuth(ClientAuth clientAuth) throws ClientNotFoundException {
        activeClientAuth = clientAuth;
        activeClient = getClient(clientAuth.getId());
    }

    public static ClientAuth getActiveClientAuth() {
        return activeClientAuth;
    }

    public static Client getActiveClient() {
        return activeClient;
    }

    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(ClientAuth.class);
            configuration.addAnnotatedClass(Transaction.class);
            configuration.addAnnotatedClass(ClientReceiver.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed.\n" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            new Thread() {
                public void run() {
                    sessionFactory = buildSessionFactory();
                }
            }.start();
        }
        return sessionFactory;
    }

    public static void close() {
        getSessionFactory().close();
    }

    public static ClientAuth getClientAuth(String username) throws ClientNotFoundException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session
                .createQuery("WHERE username = :username",
                        ClientAuth.class)
                .setParameter("username", username);

        try {
            ClientAuth client = query.getSingleResult();
            session.close();
            return client;

        } catch (NoResultException noResultException) {
            throw new ClientNotFoundException(username);
        }
    }

    public static Client getClient(int id) throws ClientNotFoundException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Client client = session.get(Client.class, id);
        session.close();

        if (client == null) {
            throw new ClientNotFoundException(id);
        }
        return client;
    }

    public static int createClient(String username, String password, String firstname, String lastname)
            throws ClientCredentialsException, ClientIdException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        int id = -1;
        int attempts = 0;
        do {
            id = (int) (Math.random() * 100000000);
            ClientAuth temp_id = session.get(ClientAuth.class, id);
            if (temp_id != null)
                id = -1;

            if (attempts++ > 3)
                throw new ClientIdException();

        } while (id == -1);

        ClientAuth auth = new ClientAuth(id, username, password);
        Client client = new Client(id, firstname, lastname);

        var tr = session.beginTransaction();
        try {
            session.persist(client);
            session.persist(auth);
            tr.commit();
            session.close();
        } catch (Exception ex) {
            tr.rollback();
        }

        return id;
    }

    public static void removeActiveClient() {
        if (getActiveClient() == null)
            return;

        Session session = HibernateUtil.getSessionFactory().openSession();
        var tr = session.beginTransaction();
        try {
            activeClient.setOpen(false);

            session.remove(activeClientAuth);
            session.merge(activeClient);
            tr.commit();
            session.close();

            activeClient = null;
            activeClientAuth = null;

        } catch (Exception ex) {
            tr.rollback();
        }
    }
}
