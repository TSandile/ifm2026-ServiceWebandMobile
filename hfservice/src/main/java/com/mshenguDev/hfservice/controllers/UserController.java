package com.mshenguDev.hfservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(path = "/api/v0.1/users")
@RequestMapping(path = "/api/users")
public class UserController {

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<String> getUsers(){
        return  ResponseEntity.ok("get Rest is working");
    }
}
