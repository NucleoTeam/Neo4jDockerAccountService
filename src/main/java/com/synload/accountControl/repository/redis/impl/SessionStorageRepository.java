package com.synload.accountControl.repository.redis.impl;

import com.synload.accountControl.domain.redis.SessionData;
import com.synload.accountControl.repository.redis.SessionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionStorageRepository implements SessionStorage {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static String SESSION_KEY = "Session";

    @Override
    public void save(SessionData sessionData) {
        redisTemplate.opsForHash().put(SESSION_KEY, sessionData.getSessionUUID(), sessionData);
    }

    @Override
    public SessionData find(String uuid) {
        return (SessionData) redisTemplate.opsForHash().get(SESSION_KEY, uuid);
    }

    @Override
    public void delete(String id) {
        redisTemplate.opsForHash().delete(SESSION_KEY, id);
    }
}
