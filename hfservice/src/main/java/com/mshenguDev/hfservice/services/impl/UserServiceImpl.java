package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Dto.LoginDto;
import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.Role;
import com.mshenguDev.hfservice.entities.User;
import com.mshenguDev.hfservice.repositories.UserRepository;
import com.mshenguDev.hfservice.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerUser(UserDto newUserDto) {
        if (newUserDto == null || newUserDto.getFirst_name() == null || newUserDto.getFirst_name().isEmpty() ||
                newUserDto.getLast_name() == null || newUserDto.getLast_name().isEmpty()) {
            throw new NullPointerException("Entity fields are empty");
        }
        try {
            User newUser = new User();
            newUser.setFirst_name(newUserDto.getFirst_name());
            newUser.setLast_name(newUserDto.getLast_name());
            newUser.setEmail(newUserDto.getEmail());
            String newPassword = passwordEncoder.encode(newUserDto.getPassword());
            newUser.setPassword(newPassword);
            newUser.setPhone(newUserDto.getPhone());
            newUser.setAddress(newUserDto.getAddress());
            newUser.setRole(Role.CUSTOMER);
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Error while registering user: " + e.getMessage());
        }
        return "SUCCESS";
    }

    @Override
    public User login(LoginDto logDetails) {
        Optional<User> user = userRepository.getByEmail(logDetails.getEmail());
        if (user.isPresent() && passwordEncoder.matches(logDetails.getPassword(), user.get().getPassword())) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
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

    @Override
    public String removeUser(Long id) {
        userRepository.deleteById(id);
        return "User removed";
    }

}
