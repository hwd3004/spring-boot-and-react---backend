package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.payload.response.MutationResponse;
import com.example.backend.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public ResponseEntity<MutationResponse<?>> createUser(User user) {
    if (!user.getPassword().equals(user.getConfirmPassword())) {
      return ResponseEntity
        .badRequest()
        .body(
          new MutationResponse<>(
            false,
            "Password and Confirm Password must be same",
            null
          )
        );
    }

    if (!user.getEmail().equals(user.getConfirmEmail())) {
      return ResponseEntity
        .badRequest()
        .body(
          new MutationResponse<>(
            false,
            "Email and Confirm Email must be same",
            null
          )
        );
    }

    if (userRepository.existsByUsername(user.getUsername())) {
      return ResponseEntity
        .badRequest()
        .body(new MutationResponse<>(false, "Username already exists", null));
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      return ResponseEntity
        .badRequest()
        .body(new MutationResponse<>(false, "Email already exists", null));
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);

    userRepository.save(user);

    return ResponseEntity.ok(
      new MutationResponse<>(true, "User created successfully", null)
    );
  }

  @Transactional(readOnly = true)
  public ResponseEntity<MutationResponse<?>> login(@RequestBody User user) {
    Optional<User> userOptional = userRepository.findByUsername(
      user.getUsername()
    );

    if (!userOptional.isPresent()) {
      return ResponseEntity
        .badRequest()
        .body(new MutationResponse<>(false, "Invalid Username", null));
    }

    if (
      !passwordEncoder.matches(
        user.getPassword(),
        userOptional.get().getPassword()
      )
    ) {
      return ResponseEntity
        .badRequest()
        .body(new MutationResponse<>(false, "Invalid Password", null));
    }

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        user.getUsername(),
        user.getPassword()
      )
    );

    if (authentication.isAuthenticated()) {
      String token = jwtService.generateToken(user.getUsername());

      return ResponseEntity.ok(
        new MutationResponse<>(true, "User logged in successfully", token)
      );
    } else {
      return ResponseEntity
        .badRequest()
        .body(
          new MutationResponse<>(false, "Invalid Username or Password", null)
        );
    }
  }
}
