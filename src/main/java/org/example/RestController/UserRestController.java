package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.Role;
import org.example.DAO.Entities.User;
import org.example.Services.UserIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/User")
@CrossOrigin(origins = "http://localhost:4200")
public class UserRestController {
    UserIService userIService;

    @PostMapping("/addUser")
    public User createUser(@RequestBody User user) {
        return userIService.createUser(user);
    }
    @PutMapping("/updateuser/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userIService.updateUser(id, updatedUser);
    }
    @GetMapping("/getuser/{id}")
    public User getUserById(@PathVariable int id) {
        return userIService.getUserById(id);
    }
    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userIService.getAllUsers();
    }
    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable int id) {
        userIService.deleteUser(id);
    }
    @PostMapping("/{id}/assign-role")
    public User assignRoleToUser(@PathVariable int id, @RequestParam Role role) {
        return userIService.assignRoleToUser(id, role);
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
