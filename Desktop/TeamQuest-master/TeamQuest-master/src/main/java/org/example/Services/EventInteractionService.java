package org.example.Services;

import org.example.DAO.ENUM.TypeEvent;

import java.util.Map;

public interface EventInteractionService {
    Map<String, Long> getAllStats();
    Map<String, Long> getStatsByCategory(TypeEvent type);
}
