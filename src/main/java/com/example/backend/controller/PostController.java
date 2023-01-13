package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/posts")
@RestController
public class PostController {

    @GetMapping
    public ResponseEntity index() {
        return new ResponseEntity("Posts", HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}
