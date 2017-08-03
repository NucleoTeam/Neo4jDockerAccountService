package com.synload.accountControl.request;

import org.owasp.encoder.Encode;

public class SessionRequest {
    private String session=null;
    private String key = null;
    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = Encode.forJava(session);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = Encode.forJava(key);
    }

    public SessionRequest(String session, String key) {
        this.session = session;
        this.key = key;
    }
}
