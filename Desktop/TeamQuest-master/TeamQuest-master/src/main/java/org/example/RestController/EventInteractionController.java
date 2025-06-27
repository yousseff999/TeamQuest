package org.example.RestController;

import lombok.RequiredArgsConstructor;
import org.example.DAO.ENUM.TypeEvent;
import org.example.Services.EventInteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class EventInteractionController {
    private final EventInteractionService service;

    @GetMapping("/interactions")
    public ResponseEntity<Map<String, Long>> getGlobalStats() {
        return ResponseEntity.ok(service.getAllStats());
    }

    @GetMapping("/interactions/{type}")
    public ResponseEntity<Map<String, Long>> getStatsByCategory(@PathVariable TypeEvent type) {
        return ResponseEntity.ok(service.getStatsByCategory(type));
    }
}
