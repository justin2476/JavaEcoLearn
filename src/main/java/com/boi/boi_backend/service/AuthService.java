package com.boi.boi_backend.service;

import com.boi.boi_backend.model.User;
import com.boi.boi_backend.repository.UserRepository;
import com.boi.boi_backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;
    // Password Encoder instance (BCrypt)
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new user with a hashed password
    public ResponseEntity<Map<String,String>> registerUser(String username, String plaintextPassword) {
        // Check if user already exists
        if (userRepository.findByUsername(username)!=null) {
            return ResponseEntity.status(404).body(Map.of("error", "User already exists!"));
        }

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(plaintextPassword);

        // Create a new user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        // Save user to the database
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", "User successfully registered"));
    }

    // Login method to validate the password
    public ResponseEntity<Map<String, String>>login(String username, String plaintextPassword) {
        // Fetch the stored user from the database
        User storedUser = userRepository.findByUsername(username);
        if (storedUser == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found!"));
        }

        // Compare the provided password with the stored hash
        if (passwordEncoder.matches(plaintextPassword, storedUser.getPassword())) {
            // Passwords match, create and return JWT token
            // Generate JWT token
            String token = jwtUtils.generateToken(username);

            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    public User getUserByName(String username) {
        User storedUser = userRepository.findByUsername(username);
        return storedUser;
    }
}
