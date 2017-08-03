package com.synload.accountControl.controller;

import com.synload.accountControl.domain.Account;
import com.synload.accountControl.request.AccountData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class InternalInformation {
    @PostMapping("/data")
    public Account getAccount(@RequestBody AccountData accountData){
        return null;
    }
}
