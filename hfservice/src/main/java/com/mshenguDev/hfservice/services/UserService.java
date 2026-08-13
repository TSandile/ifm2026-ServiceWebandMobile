package com.mshenguDev.hfservice.services;

import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
     String registerUser(UserDto newUserDto);
     Optional<User> retrieveUserById(Long id);
     Optional<User> retrieveUserByEmail(String email);
     List<User> retrieveAllUsers();

}
