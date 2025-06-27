package org.example.DAO.Repositories;

import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage,Integer> {
}
