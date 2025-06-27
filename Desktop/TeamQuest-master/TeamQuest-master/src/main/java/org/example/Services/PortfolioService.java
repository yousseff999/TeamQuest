package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Portfolio;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.PortfolioRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class PortfolioService implements PortfolioIService {
    PortfolioRepository portfolioRepository;
    UserRepository userRepository;
    Environment environment;

    @Override
    public Portfolio createPortfolioWithImage(String title, String description, MultipartFile imageFile, int userId) {
        Portfolio portfolio = new Portfolio();
        portfolio.setTitle(title);
        portfolio.setDescription(description);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newPhotoName = UUID.randomUUID() + extension;

                String uploadDir = environment.getProperty("upload.portfolio.images");
                if (uploadDir == null) {
                    throw new RuntimeException("Upload directory not set in properties.");
                }

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(newPhotoName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                portfolio.setImagePortfolio(newPhotoName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload portfolio image", e);
        }

        portfolio.setUser(user);
        return portfolioRepository.save(portfolio);
    }



    @Override
    public String getImageUrlForPortfolioByID(int idPortfolio) {
        Portfolio portfolio = portfolioRepository.findById(idPortfolio)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String baseUrl = environment.getProperty("export.portfolio.images");
        if (baseUrl == null) {
            throw new RuntimeException("Export image URL not set in properties.");
        }

        String image = portfolio.getImagePortfolio();
        return (image != null && !image.isEmpty()) ? baseUrl + image : null;
    }
    @Override
    public List<Portfolio> getAllPortfoliosWithImageUrl() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        String baseUrl = environment.getProperty("export.portfolio.images");
        if (baseUrl == null) {
            throw new RuntimeException("Export image URL not set in properties.");
        }

        for (Portfolio portfolio : portfolios) {
            String image = portfolio.getImagePortfolio();
            if (image != null && !image.isEmpty()) {
                portfolio.setImagePortfolio(baseUrl + image);
            }
        }

        return portfolios;
    }

}
