package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private Integer phone;
    private Role role;
    private Boolean enabled;
    private Date createdAt;
}
