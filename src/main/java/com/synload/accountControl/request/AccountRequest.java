package com.synload.accountControl.request;

import org.owasp.encoder.Encode;

/**
 * Created by Nathaniel on 7/23/2017.
 */
public class AccountRequest {
    private String user=null;
    private String password=null;
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
}
