package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.Services.EventIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventRestController {
    EventIService eventIService;
}
