package org.example.RestController;

import org.example.DAO.Entities.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatmessage) {
        return chatmessage;
    }
    @MessageMapping("/private-message/{username}") // same as your Angular destination
    @SendToUser("/queue/messages") // user-specific queue
    public ChatMessage sendPrivateMessage(@Payload ChatMessage message) {
        // You can add logic here if you want (save to DB, modify message, etc.)
        return message;
    }
}
