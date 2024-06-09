package com.blackfox.bunq.database;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(String name) {
        super("Client " + name + " does not exist.");
    }

    public ClientNotFoundException(int id) {
        super("Client with id " + id + " does not exist.");
    }
}
