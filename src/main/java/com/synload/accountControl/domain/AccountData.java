package com.synload.accountControl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public String password;

    @JsonIgnore
    public HashMap<String, String> permissions = new HashMap<String, String>();

    @JsonIgnore
    public HashMap<String, String> extras = new HashMap<String, String>();

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

    public HashMap<String, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, String> permissions) {
        this.permissions = permissions;
    }

    public HashMap<String, String> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, String> extras) {
        this.extras = extras;
    }
}
