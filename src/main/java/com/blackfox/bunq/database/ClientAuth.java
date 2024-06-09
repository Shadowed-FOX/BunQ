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

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    private void update() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.merge(this);
        session.getTransaction().commit();
        session.close();
    }

    public void setUsername(String username) {
        if (password.length() < 4) {
            System.out.println("Invalid username!");
            return;
        }
        System.out.println("Updating username...");
        this.username = username;
        update();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password));
    }

    public void setPassword(String password) {
        if (password.length() < 8) {
            System.out.println("Weak password!");
            return;
        }
        System.out.println("Updating password...");
        this.password = DigestUtils.sha256Hex(password);
        update();
    }
}
