package org.example.Services;

import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
public class AuthService {
     UserRepository userRepository;

     EmailService emailService;

     PasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1️⃣ Generate Reset Token and Send Email
    public void sendPasswordResetEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Email non trouvé !");
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1)); // Expiration après 1h
        userRepository.save(user);

        String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
        String emailContent = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé une réinitialisation de votre mot de passe.</p>"
                + "<p>Cliquez sur le lien ci-dessous pour le réinitialiser :</p>"
                + "<p><a href='" + resetLink + "'>Réinitialiser mon mot de passe</a></p>"
                + "<p>Ce lien expire dans 1 heure.</p>";

        emailService.sendEmail(user.getEmail(), "Réinitialisation du mot de passe", emailContent);
    }

    // 2. Réinitialisation du mot de passe
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // 1. Find user by token
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Token invalide !");
        }

        User user = userOptional.get();

        // 2. Verify token expiration
        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré !");
        }

        // 3. Update password and clear reset fields
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);  // Clear the reset token
        user.setTokenExpirationTime(null);  // Clear the expiration time

        // 4. Save changes
        userRepository.save(user);
    }
}
