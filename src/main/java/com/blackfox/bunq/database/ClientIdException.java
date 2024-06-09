package com.blackfox.bunq.database;

public class ClientIdException extends Exception {
    public ClientIdException() {
        super("Couldn't generate id.");
    }
}
