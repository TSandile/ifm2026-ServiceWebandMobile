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
    private String phone;
//    private Role role;
//    private Boolean enabled;
//    private Date createdAt;

    public String getFirst_name(){return first_name;}
    public String getLast_name(){return last_name;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getPhone(){return phone;}
//    public Role getRole(){return role;}
//    public Boolean getEnabled(){return enabled;}
//    public Date getCtreatedAt(){return createdAt;}



}
