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

    public static ClientAuth getClientAuth(String username) throws ClientNotFoundException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery("WHERE username = :username",
                            ClientAuth.class)
                    .setParameter("username", username);

            if (query.list().size() != 1) {
                session.close();
                throw new ClientNotFoundException(username);
            }

            ClientAuth client = query.list().getFirst();
            session.close();
            return client;
        }
    }

    public static Client getClient(int id) throws ClientNotFoundException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Client client = session.get(Client.class, id);
            session.close();

            if (client == null) {
                throw new ClientNotFoundException(id);
            }
            return client;
        }
    }

    public static int createClient(String username, String password, String firstname, String lastname)
            throws ClientCredentialsException, ClientIdException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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

            session.beginTransaction();
            ClientAuth auth = new ClientAuth(id, username, password);
            Client client = new Client(id, firstname, lastname);

            session.persist(auth);
            session.persist(client);
            session.getTransaction().commit();
            session.close();

            return id;
        }
    }


}
