package org.example.DAO.Repositories;

import org.example.DAO.Entities.Feedback;
import org.example.DAO.Entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    List<Question> findByDifficultyLevel(int difficultyLevel);
}
