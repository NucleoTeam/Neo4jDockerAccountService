package com.synload.accountControl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("sessionData")
public class SessionData {

    @Id String id;

    @Indexed String sessionUUID;

    @Indexed Long accountID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionUUID() {
        return sessionUUID;
    }

    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public SessionData(String sessionUUID, Long accountID) {
        this.sessionUUID = sessionUUID;
        this.accountID = accountID;
    }
}
