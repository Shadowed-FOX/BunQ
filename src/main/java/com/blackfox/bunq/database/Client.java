package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.annotations.GeneratedColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.List;

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
    @Transient//is force to not map this in data base
    public TransactionList getTransactionList;

    public Client() {
        getTransactionList = new TransactionList();
    }

    public Client(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        getTransactionList = new TransactionList();
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
        if (amount <= 0f) {
            throw new Exception("amount need be greater that 0!");
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

    //begin class------------------------------------------------------------------------------------------------------------------------------------------------
    public class TransactionList { //klasa do dostawania list bo pomyslalem ze wygodnie bd client.getTransactionList.rosnaco()
        //if you need all history for client you dont need put secondId in func
        public TransactionList() {

        }
        //get transactions sorted by date --------------------------------------------------------------------------------------------------------------

        public List<Transaction> getTransactionList() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE sender = :id OR receiver = :id",
                                Transaction.class);
                query.setParameter("id", id);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }
        }
           //overload with second id to sort thing out  !!!!!(need to test if this is working)!!!!!!!
        public List<Transaction> getTransactionList(int SecondClientId) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE (sender = :id AND receiver = :id2) OR (sender = :id2 AND receiver = :id)",
                                Transaction.class);
                query.setParameter("id", id);
                query.setParameter("id2", SecondClientId);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }

        }

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //get transactions sorted ascending------------------------------------------------------------------------
        //normal

        public List<Transaction> getTransactionListOrderByAmountASC() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE sender = :id OR receiver = :id ORDER BY amount ASC",
                                Transaction.class);
                query.setParameter("id", id);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }

        }

        //overload with second id to sort thing out  !!!!!(need to test if this is working)!!!!!!!
        public List<Transaction> getTransactionListOrderByAmountASC(int SecondClientId) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE (sender = :id AND receiver = :id2) OR (sender = :id2 AND receiver = :id) ORDER BY amount ASC",
                                Transaction.class);
                query.setParameter("id", id);
                query.setParameter("id2", SecondClientId);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }

        }

        //--------------------------------------------------------------------------------------------------------------
        //get transactions sorted decreasing------------------------------------------------------------------------
        public List<Transaction> getTransactionListOrderByAmountDESC() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE sender = :id OR receiver = :id ORDER BY amount DESC",
                                Transaction.class);
                query.setParameter("id", id);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }

        }
        //--------------------------------------------------------------------------------------------------------------
                //overload with second id to sort thing out  !!!!!(need to test if this is working)!!!!!!!
        public List<Transaction> getTransactionListOrderByAmountDESC(int SecondClientId) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                var query = session
                        .createQuery("WHERE (sender = :id AND receiver = :id2) OR (sender = :id2 AND receiver = :id) ORDER BY amount DESC",
                                Transaction.class);
                query.setParameter("id", id);
                query.setParameter("id2", SecondClientId);

                List<Transaction> list = query.list();
                session.close();

                if (list.isEmpty()) {
                    return null;
                }

                return list;
            }

        }
    }//end class---------------------------------------------------------------------------------------------------------------------------------

}
