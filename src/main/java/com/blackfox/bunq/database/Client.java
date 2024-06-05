package com.blackfox.bunq.database;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client implements Serializable {
    
    @Id
    private int id;
    String username;
    private String password;

    private String firstname;
    private String lastname;
    private float balance;
    private int colors_id;
}
