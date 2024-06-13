package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.GeneratedColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction implements Serializable {

    @Id
    private int sender;
    private int receiver;

    private String title;
    private float amount;
    @GeneratedColumn("INSERT")
    private Timestamp date;

    public Transaction() {
    }

    public Transaction(int sender, int receiver, String title, float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.amount = amount;
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
