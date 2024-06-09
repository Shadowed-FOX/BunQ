package com.blackfox.bunq.database;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_auth")
public class ClientAuth implements Serializable {

    @Id
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (password.length() < 4) {
            System.out.println("Invalid username!");
            return;
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 8) {
            System.out.println("Weak password!");
            return;
        }
        this.password = password;
    }
}
