package com.blackfox.bunq.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import jakarta.persistence.NoResultException;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.hibernate.Session;

public class HibernateUtil {

    static Client activeClient;

    public static void setActiveClient(Client client){
        activeClient = client;
    }

    public static Client getActiveClient(){
        return activeClient;
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        Logger.getLogger("com.mchange").setLevel(Level.WARNING);

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

        var tr = session.beginTransaction();
        ClientAuth auth = new ClientAuth(id, username, password);
        Client client = new Client(id, firstname, lastname);

        try {
            session.persist(auth);
            session.persist(client);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            tr.rollback();
        }

        return id;
    }
}
