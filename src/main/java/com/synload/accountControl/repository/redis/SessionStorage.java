package com.synload.accountControl.repository.redis;

import com.synload.accountControl.domain.redis.SessionData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface SessionStorage{

    void save(SessionData sessionData);

    SessionData find(String uuid);

    void delete(String id);
}
