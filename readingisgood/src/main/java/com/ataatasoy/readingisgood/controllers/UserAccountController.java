package com.ataatasoy.readingisgood.controllers;

import com.ataatasoy.readingisgood.models.UserAccount;
import com.ataatasoy.readingisgood.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountService service;

    @PostMapping("/addUserAccount")
    public Object addUserAccount(@RequestBody UserAccount userAccount){
        return service.addUserAccount(userAccount);
    }
}
