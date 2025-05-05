package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.DepartmentRepository;
import org.example.Services.DepartmentIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Department")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentRestController {
    DepartmentIService departmentIService;

    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentIService.createDepartment(department));
    }

    @GetMapping("/getall")
    public List<Department> getAllDepartments() {
        return departmentIService.getAllDepartments();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        return ResponseEntity.ok(departmentIService.getDepartmentById(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department department) {
        return ResponseEntity.ok(departmentIService.updateDepartment(id, department));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentIService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{departmentId}/addUser/{userId}")
    public ResponseEntity<Department> addUserToDepartment(@PathVariable int departmentId, @PathVariable int userId) {
        return ResponseEntity.ok(departmentIService.addUserToDepartment(departmentId, userId));
    }

    @PutMapping("/{departmentId}/removeUser/{userId}")
    public ResponseEntity<Department> removeUserFromDepartment(@PathVariable int departmentId, @PathVariable int userId) {
        return ResponseEntity.ok(departmentIService.removeUserFromDepartment(departmentId, userId));
    }
    @GetMapping("/{departmentId}/users")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable int departmentId) {
        return ResponseEntity.ok(departmentIService.getUsersByDepartment(departmentId));
    }

}
