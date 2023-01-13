package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "USER")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username", nullable = false, unique = true, length = 20)
    @NotBlank
    @Size(min = 3, max = 20)
    String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 3)
    String password;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts;


}
