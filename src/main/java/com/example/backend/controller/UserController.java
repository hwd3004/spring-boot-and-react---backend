package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.payload.response.MutationResponse;
import com.example.backend.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping
  public List<User> getFindAllUsers() {
    return userService.findAllUsers();
  }

  @PostMapping("/signup")
  public MutationResponse<User> createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @PostMapping("/login")
  public MutationResponse<User> login(@RequestBody User user) {
    return userService.login(user);
  }
}
