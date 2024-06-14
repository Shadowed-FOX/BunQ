package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.annotations.GeneratedColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client implements Serializable {

    @Id
    private int id;
    private String firstname;
    private String lastname;
    private float balance;
    @GeneratedColumn("INSERT")
    private Timestamp created_at;
    // private int colors_id;

    public Client() {
    }

    public Client(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public void update() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.merge(this);
        session.getTransaction().commit();
        session.close();
    }

    public void transferMoney(float amount, Client receiver, String title) throws Exception {
        if (this.balance < amount) {
            throw new Exception("Not enough money.");
        }
        if(amount<=0f)
        {
               throw new Exception("amount need be greater that 0");
        }
        Transaction transaction = new Transaction(id, receiver.getId(), title, amount);
        this.balance -= amount;
        receiver.balance += amount;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.persist(transaction);
        session.merge(this);
        session.merge(receiver);
        session.getTransaction().commit();
        session.close();
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public float getBalance() {
        return balance;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }
}
