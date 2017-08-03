package com.synload.accountControl.request;

import org.owasp.encoder.Encode;

/**
 * Created by Nathaniel on 7/23/2017.
 */
public class AccountRequest {
    public String user;
    public String password;
    public String session;
    public  AccountRequest(){

    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = Encode.forJava(user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Encode.forJava(password);
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = Encode.forJava(session);
    }
}
