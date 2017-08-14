package com.synload.accountControl.repository.redis;

import com.synload.accountControl.domain.redis.SessionData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionStorage extends CrudRepository<SessionData, String> {
    SessionData findByUuid(String uuid);
    List<SessionData> findByAccountIDEquals(Long accountID);
}
