package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Portfolio;
import org.example.Services.PortfolioIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Portfolio")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioRestController {
    PortfolioIService portfolioService;

    @PostMapping("/createWithImage")
    public ResponseEntity<Portfolio> createPortfolioWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("userId") int userId) {
        Portfolio created = portfolioService.createPortfolioWithImage(title, description, imageFile, userId);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/getImageUrl/{idPortfolio}")
    public ResponseEntity<String> getImageUrl(@PathVariable int idPortfolio) {
        String imageUrl = portfolioService.getImageUrlForPortfolioByID(idPortfolio);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/all")
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.getAllPortfoliosWithImageUrl();
    }
}
