package org.example.Services;

import org.example.DAO.Entities.Department;

import java.util.List;

public interface DepartmentIService {
    public Department createDepartment(Department department);
    public List<Department> getAllDepartments();
    public Department getDepartmentById(int id);
    public Department updateDepartment(int id, Department departmentDetails);
    public void deleteDepartment(int id);
}
