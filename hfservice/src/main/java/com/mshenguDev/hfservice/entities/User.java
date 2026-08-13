package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;

    @Column(unique = true)
    private String email;
    private String password;
    private Integer phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;
    private Date createdAt;

    User(String first_name, String last_name, String email, String password,Integer phone){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.CUSTOMER;
        this.createdAt = Date.from(java.time.Instant.now());
        this.enabled = true;
    }



}
