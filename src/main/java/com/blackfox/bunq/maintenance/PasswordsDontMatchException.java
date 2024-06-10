package com.blackfox.bunq.maintenance;

public class PasswordsDontMatchException extends Exception{
    PasswordsDontMatchException(){
        super("Hasła muszą być takie same");
    }
}
