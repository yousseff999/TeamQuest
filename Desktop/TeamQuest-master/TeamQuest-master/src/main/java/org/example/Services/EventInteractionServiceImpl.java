package org.example.Services;

import lombok.RequiredArgsConstructor;
import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Repositories.EventInteractionRepository;
import org.example.DAO.Repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventInteractionServiceImpl implements EventInteractionService {
    private final EventInteractionRepository repository;
    @Override
    public Map<String, Long> getAllStats() {
        return toMap(repository.countAllInteractions());
    }

    @Override
    public Map<String, Long> getStatsByCategory(TypeEvent type) {
        return toMap(repository.countInteractionsByType(type));
    }

    private Map<String, Long> toMap(List<Object[]> data) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] obj : data) {
            map.put(obj[0].toString(), (Long) obj[1]);
        }
        return map;
    }
}
