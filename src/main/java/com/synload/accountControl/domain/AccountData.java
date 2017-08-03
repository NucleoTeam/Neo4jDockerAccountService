package com.synload.accountControl.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashMap;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@NodeEntity(label = "ACCOUNT")
public class AccountData {
    @GraphId
    public Long id;

    public String user;
    private String password;

    public HashMap<String, Object> permissions = new HashMap<String, Object>();

    public HashMap<String, Object> extras = new HashMap<String, Object>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, Object> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Object> permissions) {
        this.permissions = permissions;
    }

    public HashMap<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, Object> extras) {
        this.extras = extras;
    }
}
