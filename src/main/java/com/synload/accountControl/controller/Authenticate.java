package com.synload.accountControl.controller;

import com.synload.accountControl.domain.neo4j.AccountData;
import com.synload.accountControl.domain.redis.SessionData;
import com.synload.accountControl.repository.neo4j.AccountStorage;
import com.synload.accountControl.repository.redis.SessionStorage;
import com.synload.accountControl.repository.redis.impl.SessionStorageRepository;
import com.synload.accountControl.request.SessionRequest;
import com.synload.accountControl.request.AccountRequest;
import com.synload.accountControl.utils.AccountRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@RestController
@RequestMapping("/authenticate")
public class Authenticate {
    @Autowired
    AccountStorage accountStorage;
    @Autowired
    SessionStorageRepository sessionStorage;

    @PostMapping("/login")
    public String login(@RequestBody AccountRequest accountRequest) {
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        AccountData accountData = accountStorage.getAccountByUser(accountRequest.getUser());
        if(accountData !=null){
            if(accountData.getPassword().equals(AccountRules.hash(password))){
                String uuid = UUID.randomUUID().toString();
                sessionStorage.save(new SessionData(uuid, accountData.getId()));
                return uuid;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    @PostMapping("/create")
    public String create(@RequestBody AccountRequest accountRequest){
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        if(accountStorage.getAccountByUser(user)==null) {
            AccountData accountData = new AccountData();
            accountData.setUser(user);
            accountData.setPassword(AccountRules.hash(password));
            accountStorage.save(accountData);
            return login(accountRequest);
        }else{
            return null;
        }
    }

    @PostMapping("/session")
    public boolean session(@RequestBody SessionRequest sessionRequest){
        SessionData sessionData = sessionStorage.find(sessionRequest.getSession());
        if (sessionData != null) {
            return true;
        }
        return false;
    }
    @PostMapping("/logout")
    public boolean logout(@RequestBody SessionRequest sessionRequest){
        SessionData sessionData = sessionStorage.find(sessionRequest.getSession());
        if (sessionData != null) {
            sessionStorage.delete(sessionData.getUuid());
            return true;
        }
        return false;
    }
}
