package com.synload.accountControl.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
/**
 * Created by Nathaniel on 8/6/2017.
 */
@NodeEntity(label = "Extra")
public class ExtraData {
    @GraphId
    public Long id;

    public String name;

    public String value;

    public ExtraData() {
    }

    public ExtraData(String name, String value) {
        this.name = name;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
