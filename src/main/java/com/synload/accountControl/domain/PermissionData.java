package com.synload.accountControl.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Nathaniel on 8/6/2017.
 */
@NodeEntity(label = "Permission")
public class PermissionData {

    @GraphId
    public Long id;

    public String name;

    public List<String> flags = new ArrayList<>();

    public PermissionData(String name) {
        this.name = name;
    }

    public PermissionData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFlags() {
        return flags;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }
}
