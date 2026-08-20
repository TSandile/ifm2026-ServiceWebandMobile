package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
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
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;
    private Date createdAt;

    public User() {
    }

    public User(String first_name, String last_name, String email, String password, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.CUSTOMER;
        this.createdAt = Date.from(java.time.Instant.now());
        this.enabled = true;
    }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Role getRole(){return role;}
    public void setRole(Role role){this.role = role;}
    public Date getCreatedAt(){return createdAt;}
    public Boolean getEnabled(){return enabled;}
    public void setEnabled(Boolean enabled){this.enabled = enabled;}



}
