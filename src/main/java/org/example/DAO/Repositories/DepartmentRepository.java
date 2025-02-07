package org.example.DAO.Repositories;

import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
}
