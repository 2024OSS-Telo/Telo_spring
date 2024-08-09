package soomsheo.Telo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import soomsheo.Telo.domain.Chat.PhotoMessage;
import soomsheo.Telo.domain.Chat.RequestMessage;
import soomsheo.Telo.domain.Chat.TextMessage;
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
    public TextMessage handleTextMessage(@DestinationVariable String roomID, @Payload TextMessage message) {
        System.out.println("Received message: " + message.getMessage());
        chatService.sendTextMessage(roomID, message.getSenderID(), message.getMessage());
        return message;
    }

    @MessageMapping("/chat/{roomID}/photo")
    @SendTo("/queue/{roomID}")
    public PhotoMessage handlePhotoMessage(@DestinationVariable String roomID, @Payload PhotoMessage message) {
        chatService.sendPhotoMessage(roomID, message.getSenderID(), message.getImageURL());
        return message;
    }

    @MessageMapping("/chat/repair-request")
    @SendTo("/queue/{roomID}")
    public RequestMessage handleRepairRequest(@Payload RepairRequest repairRequest) {
        String roomID = chatService.getOrCreateChatRoom(repairRequest.getLandlordID(), repairRequest.getTenantID());
        RequestMessage requestMessage = new RequestMessage(roomID, repairRequest.getTenantID(), repairRequest, LocalDateTime.now());
        chatService.sendRequestMessage(repairRequest);
        return requestMessage;
    }
}
