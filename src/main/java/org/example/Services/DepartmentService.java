package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Department;
import org.example.DAO.Repositories.DepartmentRepository;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class DepartmentService implements DepartmentIService {
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
}
