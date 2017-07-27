package com.synload.accountControl.controller;

import com.synload.accountControl.domain.Account;
import com.synload.accountControl.repository.AccountRepository;
import com.synload.accountControl.request.AccountRequest;
import com.synload.accountControl.utils.AccountRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@RestController
@RequestMapping("/authenticate")
public class Authentication {
    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/login")
    public Account login(@RequestBody AccountRequest accountRequest) {
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        Account account = accountRepository.getAccountByUser(accountRequest.getUser());
        if(account!=null){
            if(account.getPassword().equals(AccountRules.hash(password))){
                //session create
                return account;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    @RequestMapping("/create")
    public Account create(@RequestBody AccountRequest accountRequest){
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        if(accountRepository.getAccountByUser(user)==null) {
            Account account = new Account();
            account.setUser(user);
            account.setPassword(password);
            accountRepository.save(account);
            return account;
        }else{
            return null;
        }
    }
}
