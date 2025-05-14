package org.example.Services;

import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserIService {
    public User createUser(User user);
    public User updateUser(int id, User updatedUser);
    public User getUserById(int id);
    public List<User> getAllUsers();
    public void deleteUser(int id);
    public User assignRoleToUser(int id, Role role);
    public List<User> getUsersByRole(Role role);
    public List<User> searchUsers(String query);
    public long countUsersByRole(Role role);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    public List<User> getUsersByScoreDesc();
    public User getTopScorer();
}
