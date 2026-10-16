package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.Role;
import com.mshenguDev.hfservice.entities.User;

public record AuthenticatedUserDto(
        Long id,
        String first_name,
        String last_name,
        String email,
        String phone,
        Role role,
        String address) {

    public static AuthenticatedUserDto from(User user) {
        return new AuthenticatedUserDto(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getAddress());
    }
}
