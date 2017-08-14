package com.synload.accountControl.domain.neo4j;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@NodeEntity(label = "Account")
public class AccountData {

    @GraphId
    public Long id;

    public String user;

    @JsonIgnore
    public String password;

    @JsonIgnore
    @Relationship(type="HAS_PERMISSION")
    public Set<PermissionData> permissions = new HashSet<PermissionData>();

    @JsonIgnore
    @Relationship(type="HAS_EXTRA")
    public Set<ExtraData> extras = new HashSet<ExtraData>();

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

    public Set<PermissionData> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionData> permissions) {
        this.permissions = permissions;
    }

    public Set<ExtraData> getExtras() {
        return extras;
    }

    public void setExtras(Set<ExtraData> extras) {
        this.extras = extras;
    }
}
