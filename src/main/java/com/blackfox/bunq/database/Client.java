package com.blackfox.bunq.database;

import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client implements Serializable {

    @Id
    private int id;
    private String firstname;
    private String lastname;
    private float balance;
    private Date created_at;
    // private int colors_id;

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public float getBalance() {
        return balance;
    }

    public Date getCreatedAt() {
        return created_at;
    }
}
