package org.example.RestController;

import org.example.DAO.Entities.ChatMessage;
import org.example.DAO.Repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For manual message sending

    @Autowired
    private ChatRepository messageRepository; // For persisting messages

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Save the message to the database
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/private-message/{username}")
    public void sendPrivateMessage(@DestinationVariable String username, @Payload ChatMessage message) {
        // Save the message to the database
        messageRepository.save(message);
        // Send the message to the specified user's queue
        messagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }
}