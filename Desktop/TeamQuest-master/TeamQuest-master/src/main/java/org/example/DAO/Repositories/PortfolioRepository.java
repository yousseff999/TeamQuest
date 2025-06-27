package org.example.DAO.Repositories;

import org.example.DAO.Entities.Feedback;
import org.example.DAO.Entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio,Integer> {
}
