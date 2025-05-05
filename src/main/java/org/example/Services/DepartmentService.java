package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.DepartmentRepository;
import org.example.DAO.Repositories.UserRepository;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class DepartmentService implements DepartmentIService {
    UserRepository userRepository;
    DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
    }
    public Department updateDepartment(int id, Department departmentDetails) {
        Department department = getDepartmentById(id);
        department.setName(departmentDetails.getName());
        department.setScore_d(departmentDetails.getScore_d());
        department.setFloor(departmentDetails.getFloor());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(int id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }

    @Override
    public Department addUserToDepartment(int departmentId, int userId) {
        Department department = getDepartmentById(departmentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setDepartment(department);
        userRepository.save(user); // mise à jour de l'utilisateur

        return department;
    }

    @Override
    public Department removeUserFromDepartment(int departmentId, int userId) {
        Department department = getDepartmentById(departmentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (user.getDepartment() != null && user.getDepartment().getId() == departmentId) {
            user.setDepartment(null);
            userRepository.save(user);
        }

        return department;
    }
    @Override
    public List<User> getUsersByDepartment(int departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        return department.getUsers();
    }

}
