package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.UserRepository;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class UserService implements UserIService{
    UserRepository userRepository;

    public User createUser(User user) {
        // Ensure the username and email are unique
        if (userRepository.existsByUsername((user.getUsername()))) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail((user.getEmail()))) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields if provided
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
    public User assignRoleToUser(int id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }
    public long countUsersByRole(Role role) {
        return userRepository.countByRole(role);
    }


}
