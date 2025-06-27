package org.example.DAO.Entities;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GiftCardDTO {
    private Long id;
    private String title;
    private String description;
    private Integer requiredScore;
    private String imageUrl;
    private String brand;
    private String category;
    private Boolean canAfford;
    private Boolean alreadyOwned;
    private LocalDateTime createdAt;
}
