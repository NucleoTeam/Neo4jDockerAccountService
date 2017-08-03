package com.synload.accountControl.controller;

import com.synload.accountControl.domain.AccountData;
import com.synload.accountControl.request.SessionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class InternalInformation {
    @PostMapping("/data")
    public AccountData getAccount(@RequestBody SessionRequest sessionRequest){
        return null;
    }
}
