package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int sender;
    private int receiver;

    private String title;
    private float amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;

    public Transaction() {
    }

    public Transaction(int sender, int receiver, String title, float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.amount = amount;
    }

    public int getId(){
        return id;
    }

    public int getSenderId() {
        return sender;
    }

    public int getReceiverId() {
        return receiver;
    }

    public String getTitle() {
        return title;
    }

    public float getAmount() {
        return amount;
    }

    public Timestamp getDate() {
        return date;
    }
}
