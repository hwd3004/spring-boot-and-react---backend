package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.payload.response.MutationResponse;
import com.example.backend.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public MutationResponse<User> createUser(User user) {
    if (!user.getPassword().equals(user.getConfirmPassword())) {
      return new MutationResponse<>(
        false,
        "Password and Confirm Password must be same",
        null
      );
    }

    if (!user.getEmail().equals(user.getConfirmEmail())) {
      return new MutationResponse<>(
        false,
        "Email and Confirm Email must be same",
        null
      );
    }

    if (userRepository.existsByUsername(user.getUsername())) {
      return new MutationResponse<>(false, "Username already exists", null);
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      return new MutationResponse<>(false, "Email already exists", null);
    }

    userRepository.save(user);

    return new MutationResponse<>(true, "User created successfully", null);
  }

  @Transactional(readOnly = true)
  public MutationResponse<User> login(@RequestBody User user) {
    Optional<User> userDetails = userRepository.findByUsername(
      user.getUsername()
    );

    if (userDetails.isPresent()) {
      if (userDetails.get().getPassword().equals(user.getPassword())) {
        return new MutationResponse<>(
          true,
          "Login Successful",
          userDetails.get()
        );
      } else {
        return new MutationResponse<>(false, "Invalid Password", null);
      }
    } else {
      return new MutationResponse<>(false, "Invalid Username", null);
    }
  }
}
