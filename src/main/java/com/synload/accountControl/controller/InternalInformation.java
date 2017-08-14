package com.synload.accountControl.controller;

import com.synload.accountControl.domain.AccountData;
import com.synload.accountControl.domain.ExtraData;
import com.synload.accountControl.domain.PermissionData;
import com.synload.accountControl.domain.SessionData;
import com.synload.accountControl.repository.neo4j.AccountStorage;
import com.synload.accountControl.repository.neo4j.ExtraRepository;
import com.synload.accountControl.repository.neo4j.PermissionRepository;
import com.synload.accountControl.repository.redis.SessionStorage;
import com.synload.accountControl.request.SessionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class InternalInformation {
    @Autowired
    AccountStorage accountStorage;
    @Autowired
    SessionStorage sessionStorage;
    @Autowired
    ExtraRepository extraRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @PostMapping("/info")
    public AccountData getAccount(@RequestBody SessionRequest sessionRequest){
        SessionData sessionData = sessionStorage.findByUuidEquals(sessionRequest.getSession());
        if (sessionData != null) {
            return accountStorage.findOne(sessionData.getAccountID());
        }
        return null;
    }

    @PostMapping("/permission/get")
    public Object getPermission(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        if(accountData!=null){
            Set<PermissionData> perms = accountData.getPermissions().parallelStream().filter(e->{
                return e.getName().equalsIgnoreCase(sessionRequest.getKey());
            }).collect(Collectors.toSet());
            if(perms.size()>0){
                return perms.iterator().next();
            }
        }
        return null;
    }

    @PostMapping("/extra/get")
    public Object getExtra(@RequestBody SessionRequest sessionRequest){
        AccountData accountData = getAccount(sessionRequest);
        if(accountData!=null){
            Set<ExtraData> extras = accountData.getExtras().parallelStream().filter(e->{
                return e.getName().equalsIgnoreCase(sessionRequest.getKey());
            }).collect(Collectors.toSet());
            if(extras.size()>0){
                return extras.iterator().next();
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
            Set<PermissionData> permissions = accountData.getPermissions().parallelStream().filter(e->{
                return e.getName().equalsIgnoreCase(key);
            }).collect(Collectors.toSet());
            if(permissions.size()==0){
                PermissionData permission = permissionRepository.getByName(key);
                if(permission==null){
                    permission = new PermissionData(key);
                }
                permission.setFlags(Arrays.asList(value.split(",")));
                permissionRepository.save(permission);
                accountData.getPermissions().add(permission);
            }else{
                PermissionData permission = permissions.iterator().next();
                permission.setFlags(Arrays.asList(value.split(",")));
                permissionRepository.save(permission);
            }
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
            Set<ExtraData> extras = accountData.getExtras().parallelStream().filter(e->{
                return e.getName().equalsIgnoreCase(key);
            }).collect(Collectors.toSet());
            if(extras.size()==0){
                ExtraData ed = new ExtraData(key, value);
                extraRepository.save(ed);
                accountData.getExtras().add(ed);
            }else{
                ExtraData ed = extras.iterator().next();
                ed.setValue(value);
                extraRepository.save(ed);
            }
            accountStorage.save(accountData);
            return true;
        }
        return false;
    }

}
