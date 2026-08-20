package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import com.mshenguDev.hfservice.repositories.UserRepository;
import com.mshenguDev.hfservice.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registerUser(UserDto newUserDto) {
        if (newUserDto == null || newUserDto.getFirst_name() == null || newUserDto.getFirst_name().isBlank() ||
                newUserDto.getLast_name() == null || newUserDto.getLast_name().isBlank()) {
            throw new NullPointerException("Entity fields are empty");
        }
        try {
            User newUser = new User(newUserDto.getFirst_name(), newUserDto.getLast_name(), newUserDto.getEmail(), newUserDto.getPassword(), newUserDto.getPhone());
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
