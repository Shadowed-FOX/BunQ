package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.GeneratedColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction implements Serializable {

    
    private int sender;
    private int receiver;

    private String title;
    private float amount;
  @Id
    private Timestamp date;

    public Transaction() {
    }

    public Transaction(int sender, int receiver, String title, float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.amount = amount;
        this.date=new Timestamp(System.currentTimeMillis());
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
