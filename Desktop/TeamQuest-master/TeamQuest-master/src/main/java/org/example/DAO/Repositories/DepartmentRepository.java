package org.example.DAO.Repositories;

import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    @Query("SELECT d.name, d.score_d FROM Department d ORDER BY d.score_d DESC")
    List<Object[]> findAllDepartmentNamesAndScoresOrderedByScoreDesc();
    long count();
}
