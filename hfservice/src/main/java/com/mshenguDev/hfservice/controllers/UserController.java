package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import com.mshenguDev.hfservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor
@CrossOrigin
@RestController
//@RequestMapping(path = "/api/v0.1/users")
@RequestMapping(path = "/api/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(path = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        String result = userService.registerUser(userDto);
        if(!result.equals("SUCCESS")){
            return ResponseEntity.badRequest().body("Error while adding user");
        }
        return ResponseEntity.ok("New user Added");
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<List<User>> getUsers(){
        return  ResponseEntity.ok(userService.retrieveAllUsers());
    }
}
