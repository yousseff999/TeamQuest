package org.example.Services;

import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthService {
     UserRepository userRepository;
     UserService userService;
     EmailService emailService;
    private static final int TOKEN_EXPIRATION_HOURS = 24;
     PasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder,@Lazy UserService userService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        // Generate and set new token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));

        // Save with explicit flush
        userRepository.saveAndFlush(user);

        // Send email - inline content creation
        String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
        String emailContent = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé une réinitialisation de votre mot de passe.</p>"
                + "<p>Cliquez sur le lien ci-dessous pour le réinitialiser :</p>"
                + "<p><a href='" + resetLink + "'>Réinitialiser mon mot de passe</a></p>"
                + "<p>Ce lien expire dans 1 heure.</p>";

        emailService.sendEmail(user.getEmail(), "Password Reset", emailContent);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // Verify token expiration
        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpirationTime(null);

        // Save with flush
        userRepository.saveAndFlush(user);
    }

    public User signUp(String name, String email, String password, String confirmPassword) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new RuntimeException("Confirm password is required");
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new RuntimeException("Invalid email format");
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        // Create user object
        User user = new User();
        user.setUsername(email); // Assuming username is same as email, adjust if needed
        user.setEmail(email);
        user.setPassword(password); // Will be encoded in UserService
        user.setUsername(name);
        user.setRole(Role.USER); // Default role

        return userService.createUser(user);
    }


    }



