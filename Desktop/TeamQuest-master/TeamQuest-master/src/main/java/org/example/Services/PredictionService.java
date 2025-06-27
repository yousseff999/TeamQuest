package org.example.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@AllArgsConstructor
@Service
public class PredictionService {
    private final RestTemplate restTemplate;
    private final String flaskApiUrl = "http://localhost:5001/predict";

    public PredictionService() {
        this.restTemplate = new RestTemplate();
    }

    public String predict(Map<String, Object> input) {
        try {
            Map<String, String> response = restTemplate.postForObject(flaskApiUrl, input, Map.class);
            if (response == null || !response.containsKey("status")) {
                throw new RuntimeException("Invalid response from prediction API");
            }
            return response.get("status");
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Failed to connect to prediction API: " + e.getMessage());
        }
    }
}
