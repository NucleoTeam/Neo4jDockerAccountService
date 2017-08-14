package com.synload.accountControl.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("sessions")
public class SessionData implements Serializable {

    @Id String id;

    @Indexed String uuid;

    @Indexed Long accountID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionUUID() {
        return uuid;
    }

    public void setSessionUUID(String sessionUUID) {
        this.uuid = sessionUUID;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public SessionData(String sessionUUID, Long accountID) {
        this.uuid = sessionUUID;
        this.accountID = accountID;
    }

    public SessionData() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
