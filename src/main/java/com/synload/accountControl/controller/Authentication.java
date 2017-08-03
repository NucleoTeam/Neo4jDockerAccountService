package com.synload.accountControl.controller;

import com.synload.accountControl.domain.Account;
import com.synload.accountControl.repository.AccountRepository;
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
public class Authentication {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/login")
    public String login(@RequestBody AccountRequest accountRequest, HttpServletRequest req) {
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        if(req.getSession().getAttribute("account")!=null){
            return (String) req.getSession().getAttribute("uuid");
        }
        Account account = accountRepository.getAccountByUser(accountRequest.getUser());
        if(account!=null){
            if(account.getPassword().equals(AccountRules.hash(password))){
                //session create
                req.getSession().setAttribute("account", account.getId());
                String uuid = UUID.randomUUID().toString();
                req.getSession().setAttribute("uuid", uuid);
                return uuid;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    @PostMapping("/create")
    public String create(@RequestBody AccountRequest accountRequest, HttpServletRequest req){
        String user = accountRequest.getUser();
        String password = accountRequest.getPassword();
        if(!AccountRules.password(password) || !AccountRules.user(user)){
            return null;
        }
        if(req.getSession().getAttribute("account")!=null){
            return (String) req.getSession().getAttribute("uuid");
        }
        if(accountRepository.getAccountByUser(user)==null) {
            Account account = new Account();
            account.setUser(user);
            account.setPassword(AccountRules.hash(password));
            accountRepository.save(account);
            req.getSession().setAttribute("account", account.getId());
            String uuid = UUID.randomUUID().toString();
            req.getSession().setAttribute("uuid", uuid);
            return uuid;
        }else{
            return null;
        }
    }

    @PostMapping("/session")
    public String session(HttpServletRequest req){
        if(req.getSession().getAttribute("account")!=null){
            return (String) req.getSession().getAttribute("uuid");
        }else{
            return null;
        }
    }
    @PostMapping("/logout")
    public boolean logout(HttpServletRequest req){
        if(req.getSession().getAttribute("account")!=null){
            req.getSession().invalidate();
            return true;
        }else{
            return false;
        }
    }
}
