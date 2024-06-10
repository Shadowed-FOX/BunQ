package com.blackfox.bunq.maintenance;

import com.blackfox.bunq.database.ClientIdException;
import com.blackfox.bunq.database.ClientUsernameException;
import com.blackfox.bunq.database.HibernateUtil;

public class AuthenticationService {
    public static void Register(String username, String firstName, String lastName, String password1, String password2) throws PasswordsDontMatchException, ClientUsernameException, ClientIdException {
        if(!password1.equals(password2))
            throw new PasswordsDontMatchException();
        HibernateUtil.createClient(username, password1, firstName, lastName);
    }
}
