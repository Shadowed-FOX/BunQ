package com.blackfox.bunq.database;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.AbstractList;

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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ClientReceiver> receivers;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;
    // private int colors_id;

    @ColumnDefault("TRUE")
    private boolean is_open;

    public Client() {
    }

    public Client(int id, String firstname, String lastname) throws ClientCredentialsException {
        this.id = id;
        setFirstname(firstname);
        setLastname(lastname);
    }

    public void makeTransaction(float amount, Client receiver, String title) throws Exception {
        if (!receiver.isOpen()) {
            throw new Exception("Receiver's account is closed");
        }

        if (this.balance < amount) {
            throw new Exception("Not enough money.");
        }

        if (amount <= 0.f) {
            throw new Exception("Amount needs to be greater that 0!");
        }

        Transaction transaction = new Transaction(id, receiver.getId(), title, amount);
        this.balance -= amount;
        receiver.balance += amount;

        Client client = this;

        new Thread() {
            public void run() {
                Session session = HibernateUtil.getSessionFactory().openSession();
                var tr = session.beginTransaction();
                try {
                    session.persist(transaction);
                    session.merge(client);
                    session.merge(receiver);
                    session.getTransaction().commit();
                    session.close();
                } catch (Exception ex) {
                    tr.rollback();
                }
            }
        }.start();
    }

    public List<Transaction> getTransactions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session.createQuery("WHERE sender = :id OR receiver = :id", Transaction.class)
                .setParameter("id", id);

        List<Transaction> list = query.list();
        session.close();

        return list;
    }

    public List<Transaction> getTransactions(Timestamp since, Timestamp until) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session
                .createQuery("WHERE sender = :id OR receiver = :id AND date BETWEEN :since AND :until",
                        Transaction.class)
                .setParameter("id", id)
                .setParameter("since", since)
                .setParameter("until", until);

        List<Transaction> list = query.list();
        session.close();

        return list;
    }

    public List<Transaction> getTransactions(TransactionType type) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session
                .createQuery(
                        "WHERE " + ((type == TransactionType.SENT) ? "sender"
                                : "receiver") + " = :id",
                        Transaction.class)
                .setParameter("id", id);

        List<Transaction> list = query.list();
        session.close();

        return list;
    }

    public List<Transaction> getTransactions(TransactionType type, Timestamp since, Timestamp until) {
        Session session = HibernateUtil.getSessionFactory().openSession();
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
    }

    public void addReceiver(ClientReceiver receiver) {
        receivers.add(receiver);
        receiver.setClient(this);
    }

    public List<Transaction> getTransactions(String filter) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String hql = "FROM Transaction t WHERE t.receiver IN (SELECT c.id FROM Client c WHERE c.firstname = :filter OR c.lastname = :filter) OR t.title  LIKE :filterAnyWhere";
        var query = session.createQuery(hql, Transaction.class);

        query.setParameter("filter", filter);
        query.setParameter("filterAnyWhere", "%" + filter + "%");
        List<Transaction> list = query.list();
        session.close();

        return list;
    }

    public void SafeReciver(Client receiver) {

        for (var i : getRecivers()) {
            if (i.getReceiverId() == receiver.getId()) {
                System.out.println("reciver already is in list");
                return;
            }
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        ClientReceiver newReceiver = new ClientReceiver(this, receiver.getId());

        this.addReceiver(newReceiver);

        session.persist(newReceiver);

        session.merge(this);

        session.merge(receiver);

        transaction.commit();
        session.close();
    }

    public List<ClientReceiver> getRecivers() {
        return receivers;
    }
 
    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    private void setFirstname(String firstname) throws ClientCredentialsException {
        this.firstname = parseName("Firstname", firstname);
    }

    public void updateFirstname(String firstname) throws ClientCredentialsException {
        setFirstname(firstname);
        update();
    }

    private void update() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var tr = session.beginTransaction();
        session.merge(this);
        tr.commit();
        session.close();
    }

    public String getLastname() {
        return lastname;
    }

    private void setLastname(String lastname) throws ClientCredentialsException {
        this.lastname = parseName("Lastname", lastname);
    }

    public void updateLastname(String lastname) throws ClientCredentialsException {
        setLastname(lastname);
        update();
    }

    private String parseName(String debug, String name) throws ClientCredentialsException {
        String formatted = name.trim();
        if (formatted.length() < 2) {
            throw new ClientCredentialsException(debug + " must contain at least 2 characters");
        }
        if (formatted.contains(" ")) {
            throw new ClientCredentialsException(debug + " cannot contain any white characters");
        }
        return formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1).toLowerCase();
    }

    public float getBalance() {
        return balance;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public boolean isOpen() {
        return is_open;
    }

    protected void setOpen(boolean value) {
        is_open = value;
    }
}
