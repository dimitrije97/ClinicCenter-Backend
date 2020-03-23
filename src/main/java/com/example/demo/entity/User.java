package com.example.demo.entity;

import com.example.demo.util.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.print.Doc;
import java.util.Date;

@Entity(name = "user_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String country;

    private String city;

    private String address;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String ssn;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Patient patient;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Date firstTimeLoggedIn;

    private boolean deleted;
}
