package org.example.DAO.Repositories;

import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    // Find a user by username
    Optional<User> findByUsername(String username);
    // Find a user by email
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Override
    List<User> findAll();
     List <User> findUsersById(int id);
    List<User> findByRole(Role role);
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
    long countByRole(Role role);
}
