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
    private Role role;
    private Boolean enabled;
    private Date createdAt;





}
