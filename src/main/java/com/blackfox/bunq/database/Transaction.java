package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction implements Serializable {
    
    @Id
    private int sender;
    private int receiver;

    private String title;
    private float amount;
    private Date date;

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

    public Date getDate() {
        return date;
    }
}
