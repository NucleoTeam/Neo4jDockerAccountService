package com.synload.accountControl.repository;

import com.synload.accountControl.domain.Account;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@Repository
public interface AccountRepository extends GraphRepository<Account> {
    Account getAccountByUser(String user);
}
