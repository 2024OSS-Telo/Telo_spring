package soomsheo.Telo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import soomsheo.Telo.domain.Chat.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.ChatService;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {
    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{roomID}/text")
    @SendTo("/queue/{roomID}")
    public ChatMessage handleTextMessage(@DestinationVariable String roomID, @Payload TextMessage message) {
        return chatService.saveTextMessage(roomID, message.getSenderID(), message.getMessage());
    }

    @MessageMapping("/chat/{roomID}/photo")
    @SendTo("/queue/{roomID}")
    public PhotoMessage handlePhotoMessage(@DestinationVariable String roomID, @Payload PhotoMessage message) {
        chatService.savePhotoMessage(roomID, message.getSenderID(), message.getImageURL());
        return message;
    }
}
