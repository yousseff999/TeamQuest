package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.User;
import org.example.Services.UserIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/User")
@AllArgsConstructor
public class UserRestController {

    private final UserIService userIService;

    @PostMapping("/addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userIService.createUser(user), HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        try {
            return new ResponseEntity<>(userIService.updateUser(id, updatedUser), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(userIService.getUserById(id), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userIService.getAllUsers();
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userIService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/assign-role")
    public ResponseEntity<User> assignRoleToUser(@PathVariable int id, @RequestParam Role role) {
        try {
            return new ResponseEntity<>(userIService.assignRoleToUser(id, role), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/role")
    public List<User> getUsersByRole(@RequestParam Role role) {
        return userIService.getUsersByRole(role);
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String query) {
        return userIService.searchUsers(query);
    }

    @GetMapping("/count-by-role")
    public long countUsersByRole(@RequestParam Role role) {
        return userIService.countUsersByRole(role);
    }
}
