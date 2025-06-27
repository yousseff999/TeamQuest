package org.example.Services;

import org.example.DAO.Entities.Portfolio;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PortfolioIService {
    public Portfolio createPortfolioWithImage(String title, String description, MultipartFile imageFile, int userId);
    public String getImageUrlForPortfolioByID(int idPortfolio);
    public List<Portfolio> getAllPortfoliosWithImageUrl();
}
