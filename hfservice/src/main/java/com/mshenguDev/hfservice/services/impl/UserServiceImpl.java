package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import com.mshenguDev.hfservice.repositories.UserRepository;
import com.mshenguDev.hfservice.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public String registerUser(UserDto newUserDto) {
        if (newUserDto.getFirst_name().isBlank() && newUserDto.getFirst_name().isBlank()) {
            throw new NullPointerException("Entity fields are empty");
        }
        try {
            User newUser = new User();
            newUser.setFirst_name(newUserDto.getFirst_name());
            newUser.setLast_name(newUserDto.getLast_name());
            newUser.setEmail(newUserDto.getEmail());
            newUser.setPassword(newUserDto.getPassword());
            newUser.setPhone(newUserDto.getPhone());
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Error while registering user: " + e.getMessage());
        }
        return "SUCCESS";
    }

    @Override
    public Optional<User> retrieveUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> retrieveUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

}



