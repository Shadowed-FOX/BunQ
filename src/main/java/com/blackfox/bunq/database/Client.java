package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.query.Query;

@Entity
public class Client implements Serializable {

    public enum TransactionType {
        SENT, RECEIVED
    }

    @Id
    private int id;
    private String firstname;
    private String lastname;
    private float balance;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;
    // private int colors_id;

    public Client() {
    }

    public Client(int id, String firstname, String lastname) {
        this.id = id;
        setFirstname(firstname);
        setLastname(lastname);
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
        if (amount <= 0.f) {
            throw new Exception("Amount needs to be greater that 0!");
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

    public List<Transaction> getTransactions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session.createQuery("WHERE sender = :id OR receiver = :id", Transaction.class)
                    .setParameter("id", id);
            List<Transaction> list = query.list();
            session.close();

            return list;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return null;
    }

    public List<Transaction> getTransactions(Timestamp since, Timestamp until) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery("WHERE sender = :id OR receiver = :id AND date BETWEEN :since AND :until",
                            Transaction.class)
                    .setParameter("id", id)
                    .setParameter("since", since)
                    .setParameter("until", until);

            List<Transaction> list = query.list();
            session.close();

            return list;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return null;
    }

    public List<Transaction> getTransactions(TransactionType type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery(
                            "WHERE " + ((type == TransactionType.SENT) ? "sender"
                                    : "receiver") + " = :id",
                            Transaction.class)
                    .setParameter("id", id);

            List<Transaction> list = query.list();
            session.close();

            return list;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return null;
    }

    public List<Transaction> getTransactions(TransactionType type, Timestamp since, Timestamp until) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session
                    .createQuery(
                            "WHERE " + ((type == TransactionType.SENT) ? "sender"
                                    : "receiver") + " = :id AND date BETWEEN :since AND :until",
                            Transaction.class)
                    .setParameter("id", id)
                    .setParameter("since", since)
                    .setParameter("until", until);

            List<Transaction> list = query.list();
            session.close();

            return list;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return null;
    }

    public List<Transaction> getTransactions(String filter) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String hql = "FROM Transaction t WHERE t.receiver IN (SELECT c.id FROM Client c WHERE c.firstname = :filter OR c.lastname = :filter) OR t.title  LIKE :filterAnyWhere";
        Query query = session.createQuery(hql, Transaction.class);

        query.setParameter("filter", filter);
        query.setParameter("filterAnyWhere", "%" + filter + "%");
        List<Transaction> list = query.list();
        session.close();

        return list;

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
