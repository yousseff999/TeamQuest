package org.example.DAO.Repositories;

import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
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
    Optional<User> findByResetToken(String resetToken);
    @Query("SELECT u FROM User u ORDER BY u.score_u DESC")
    List<User> findAllByOrderByScoreUDesc();
    @Query("SELECT u FROM User u WHERE u.score_u = (SELECT MAX(u2.score_u) FROM User u2)")
    User findTopByOrderByScore_uDesc();
    long count();
    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.events e " +
            "LEFT JOIN EventInteraction i ON i.user = u " +
            "GROUP BY u.id " +
            "ORDER BY (COUNT(DISTINCT e) + COUNT(i)) DESC")
    List<User> findUsersByEngagement();

}
