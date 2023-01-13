package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Entity
@Table(name = "POST")
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title", nullable = false)
    @NotNull
    String title;


    @Column(name = "content", nullable = false, columnDefinition = "blob")
    @NotNull
    String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;
}
