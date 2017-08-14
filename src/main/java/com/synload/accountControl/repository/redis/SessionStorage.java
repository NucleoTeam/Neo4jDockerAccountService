package com.synload.accountControl.repository.redis;

import com.synload.accountControl.domain.SessionData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionStorage extends CrudRepository<SessionData, String> {
    SessionData findOneByUuidEquals(String uuid);
    List<SessionData> findByAccountIDEquals(Long accountID);
}
