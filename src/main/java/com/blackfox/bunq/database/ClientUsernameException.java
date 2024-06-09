package com.blackfox.bunq.database;

public class ClientUsernameException extends Exception {
    ClientUsernameException() {
        super("username already in use");
    }
}
