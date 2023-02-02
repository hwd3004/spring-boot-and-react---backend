package com.example.backend.controller;

import com.example.backend.payload.response.MutationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({ "", "/" })
@RestController
public class IndexController {

  @GetMapping
  public ResponseEntity<MutationResponse<?>> index() {
    return ResponseEntity.ok(
      new MutationResponse<>(true, "Here is Spring Boot", null)
    );
  }
}
