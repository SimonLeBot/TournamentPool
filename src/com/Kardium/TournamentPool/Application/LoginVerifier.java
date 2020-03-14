package com.Kardium.TournamentPool.Application;

import com.Kardium.TournamentPool.Storage.AlreadyRegisteredUser;
import com.Kardium.TournamentPool.Storage.NotRegisteredUserException;
import com.Kardium.TournamentPool.Storage.Passwords;

public class LoginVerifier {
    private final String name;
    private final int password;
    private static final Passwords passwords = new Passwords();

    public LoginVerifier(String name, int password) {
        this.name = name;
        this.password = password;
    }

    public boolean isValidPassword() {
        try {
            return password == passwords.getPassword(name);
        } catch (NotRegisteredUserException e) {
            try {
                passwords.registerNewUser(name, password);
                return password == passwords.getPassword(name);
            } catch (AlreadyRegisteredUser aru) {
                return false;
            }
        }
    }
}
