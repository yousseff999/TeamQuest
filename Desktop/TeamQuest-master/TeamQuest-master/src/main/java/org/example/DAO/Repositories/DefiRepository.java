package org.example.DAO.Repositories;

import org.example.DAO.Entities.Defi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefiRepository extends JpaRepository<Defi, Integer> {

    @Query(value = "SELECT * FROM defi ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Defi findLastCreatedDefi();

    @Query("SELECT d FROM Defi d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.theme) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Defi> searchDefisByKeyword(@Param("keyword") String keyword);

}
