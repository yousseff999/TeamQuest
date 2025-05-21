package org.example.RestController;

import org.example.Services.PredictionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionController {
    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    @CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
    public String predict(@RequestBody Map<String, Object> input) {
        return predictionService.predict(input);
    }
}
