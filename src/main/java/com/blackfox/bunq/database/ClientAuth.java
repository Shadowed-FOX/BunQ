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

    private void setUsername(String username) throws ClientCredentialsException {
        try {
            HibernateUtil.getClientAuth(username);
            throw new ClientCredentialsException("Username already in use");
        } catch (ClientNotFoundException ex) {
        }

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

    private void update() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.merge(this);
        session.close();
    }

    public void updateUsername(String username) throws ClientCredentialsException {
        setUsername(username);
        update();
    }

    public void updatePassword(String password) throws ClientCredentialsException {
        setPassword(password);
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
