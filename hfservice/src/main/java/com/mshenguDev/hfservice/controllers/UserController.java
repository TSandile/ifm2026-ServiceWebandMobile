package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(path = "/api/v0.1/users")
@RequestMapping(path = "/api/users")
public class UserController {


    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) throws Exception{
        if(userDto == null){
            throw new NullPointerException("User attributes are empty");
        }

        return ResponseEntity.ok("User created Successfully" + userDto.getFirst_name());
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<String> getUsers(){

        return  ResponseEntity.ok("get Rest is working");
    }
}
