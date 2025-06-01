package org.example.RestController;

import org.example.DAO.Entities.ExchangeRequestDTO;
import org.example.DAO.Entities.GiftCardDTO;
import org.example.Services.GiftCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gift-cards")
@CrossOrigin(origins = "http://localhost:4200")
public class GiftCardController {

    @Autowired
    private GiftCardService giftCardService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<GiftCardDTO>> getGiftCards(@PathVariable Integer userId) {
        try {
            List<GiftCardDTO> giftCards = giftCardService.getAllGiftCards(userId);
            return ResponseEntity.ok(giftCards);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/exchange")
    public ResponseEntity<Map<String, String>> exchangeGiftCard(@RequestBody ExchangeRequestDTO request) {
        try {
            String result = giftCardService.exchangeGiftCard(
                    request.getGiftCardId(),
                    request.getUserId()
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}
