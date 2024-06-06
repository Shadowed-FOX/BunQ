package com.blackfox.bunq.database;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client implements Serializable {
    
    @Id
    private int id;
    private String username;

    private String firstname;
    private String lastname;
    private float balance;
    // private int colors_id;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
}
