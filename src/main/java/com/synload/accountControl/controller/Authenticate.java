package com.synload.accountControl.controller;

import com.synload.accountControl.domain.Account;
import com.synload.accountControl.domain.SessionData;
import com.synload.accountControl.repository.AccountRepository;
import com.synload.accountControl.repository.SessionStorage;
import com.synload.accountControl.request.AccountData;
import com.synload.accountControl.request.AccountRequest;
import com.synload.accountControl.utils.AccountRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@RestController
@RequestMapping("/authenticate")
public class Authenticate {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SessionStorage sessionStorage;

    @PostMapping("/login")
    public String login(@RequestBody AccountRequest accountRequest) {
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        Account account = accountRepository.getAccountByUser(accountRequest.getUser());
        if(account!=null){
            if(account.getPassword().equals(AccountRules.hash(password))){
                String uuid = UUID.randomUUID().toString();
                sessionStorage.save(new SessionData(uuid, account.getId()));
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
        if(accountRepository.getAccountByUser(user)==null) {
            Account account = new Account();
            account.setUser(user);
            account.setPassword(AccountRules.hash(password));
            accountRepository.save(account);
            return login(accountRequest);
        }else{
            return null;
        }
    }

    @PostMapping("/session")
    public boolean session(@RequestBody AccountData accountData){
        SessionData sessionData = sessionStorage.findBySessionUUID(accountData.getSession());
        if(sessionData!=null){
            return true;
        }else{
            return false;
        }
    }
    @PostMapping("/logout")
    public boolean logout(@RequestBody AccountData accountData){
        SessionData sessionData = sessionStorage.findBySessionUUID(accountData.getSession());
        if(sessionData!=null){
            sessionStorage.delete(sessionData);
            return true;
        }else{
            return false;
        }
    }
}
