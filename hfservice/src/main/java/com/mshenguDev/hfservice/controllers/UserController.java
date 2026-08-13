package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
//@RequestMapping(path = "/api/v0.1/users")
@RequestMapping(path = "/api/users")
public class UserController {

    @PostMapping(path = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        if(userDto == null){
            throw new NullPointerException("Entify fields are empty");
        }
        return ResponseEntity.ok("New user Added");
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<String> getUsers(){

        return  ResponseEntity.ok("get Rest is working");
    }
}
