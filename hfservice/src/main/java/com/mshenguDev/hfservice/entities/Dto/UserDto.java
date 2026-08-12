package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.Role;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
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
