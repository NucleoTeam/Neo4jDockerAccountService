package com.synload.accountControl.repository.neo4j;

import com.synload.accountControl.domain.AccountData;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@Repository
public interface AccountStorage extends GraphRepository<AccountData> {
    AccountData getAccountByUser(String user);
}
