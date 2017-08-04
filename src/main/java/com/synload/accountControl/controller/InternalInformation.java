package com.synload.accountControl.controller;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.synload.accountControl.domain.AccountData;
import com.synload.accountControl.domain.SessionData;
import com.synload.accountControl.repository.AccountStorage;
import com.synload.accountControl.repository.SessionStorage;
import com.synload.accountControl.request.SessionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class InternalInformation {
    @Autowired
    AccountStorage accountStorage;
    @Autowired
    SessionStorage sessionStorage;

    @PostMapping("/info")
    public AccountData getAccount(@RequestBody SessionRequest sessionRequest){
        SessionData sessionData = sessionStorage.findBySessionUUID(sessionRequest.getSession());
        if(sessionData!=null){
            return accountStorage.findOne(sessionData.getAccountID());
        }
        return null;
    }

    @PostMapping("/permission/get")
    public Object getPermission(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        if(accountData!=null){
            if(accountData.getPermissions().containsKey(sessionRequest.getKey())){
                return accountData.getPermissions().get(sessionRequest.getKey());
            }
        }
        return null;
    }

    @PostMapping("/extra/get")
    public Object getExtra(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        if(accountData!=null){
            if(accountData.getExtras().containsKey(sessionRequest.getKey())){
                return accountData.getExtras().get(sessionRequest.getKey());
            }
        }
        return null;
    }

    @PostMapping("/permission/set")
    public boolean setPermission(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        String value = sessionRequest.getValue();
        String key = sessionRequest.getKey();
        if(accountData!=null && value!=null && key!=null && (getPermission(sessionRequest)==null || sessionRequest.isOverwrite())){
            accountData.getPermissions().put( key, value );
            accountStorage.save(accountData);
            return true;
        }
        return false;
    }

    @PostMapping("/extra/set")
    public Object setExtra(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        String value = sessionRequest.getValue();
        String key = sessionRequest.getKey();
        if(accountData!=null && value!=null && key!=null && (getExtra(sessionRequest)==null || sessionRequest.isOverwrite())){
            accountData.getExtras().put( key, value );
            accountStorage.save(accountData);
            return true;
        }
        return false;
    }

}
