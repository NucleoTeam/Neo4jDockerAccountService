package com.synload.accountControl.repository;

import com.synload.accountControl.domain.SessionData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionStorage extends CrudRepository<SessionData, String> {
    SessionData findBySessionUUID(String uuid);
    List<SessionData> findByAccountID(Long accountID);
}
