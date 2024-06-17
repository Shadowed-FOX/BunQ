package com.blackfox.bunq.database;

import java.io.Serializable;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_auth")
public class ClientAuth implements Serializable {

    @Id
    private int id;
    private String username;
    private String password;

    public ClientAuth() {
    }

    public ClientAuth(int id, String username, String password) throws ClientCredentialsException {
        this.id = id;
        setUsername(username);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    private void update() {
        ClientAuth clientAuth = this;

        new Thread() {
            @Override
            public void run() {
                Session session = HibernateUtil.getSessionFactory().openSession();
                var tr = session.beginTransaction();
                try {
                    session.merge(clientAuth);
                    session.getTransaction().commit();
                    session.close();
                } catch (Exception ex) {
                    tr.rollback();
                    System.err.println(ex.getCause());
                }
            }
        }.start();
    }

    private void setUsername(String username) throws ClientCredentialsException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session
                .createQuery("SELECT 1 FROM ClientAuth WHERE username = :username",
                        Long.class)
                .setParameter("username", username);

        if (query.uniqueResult() != null) {
            session.close();
            throw new ClientCredentialsException("Username already in use.");
        }
        session.close();

        if (username.length() < 4) {
            throw new ClientCredentialsException("Invalid username.");
        }
        this.username = username;
    }

    public void updateUsername(String username) throws ClientCredentialsException {
        setUsername(username);
        update();
    }

    public void updatePassword(String password) throws ClientCredentialsException {
        setUsername(password);
        update();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password + id));
    }

    private void setPassword(String password) throws ClientCredentialsException {
        if (password.length() < 8) {
            throw new ClientCredentialsException("Invalid password.");
        }
        this.password = DigestUtils.sha256Hex(password + id);
    }
}
