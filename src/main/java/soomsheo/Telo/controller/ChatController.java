package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import soomsheo.Telo.domain.Chat.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.ChatService;
import soomsheo.Telo.service.RepairRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final RepairRequestService repairRequestService;

    @Autowired
    public ChatController(ChatService chatService, RepairRequestService repairRequestService) {
        this.chatService = chatService;
        this.repairRequestService = repairRequestService;
    }

    @GetMapping("/chat")
    public List<ChatRoom> getChatRoomList() {
        return chatService.getChatRoomList();
    }

    @MessageMapping("/{roomID}/text")
    @SendTo("/queue/{roomID}")
    public TextMessage handleTextMessage(@DestinationVariable String roomID, TextMessage message) {
        chatService.sendTextMessage(roomID, message.getSenderID(), message.getMessage());
        return message;
    }

    @MessageMapping("/{roomID}/photo")
    @SendTo("/queue/{roomID}")
    public PhotoMessage handlePhotoMessage(@DestinationVariable String roomID, PhotoMessage message) {
        chatService.sendPhotoMessage(roomID, message.getSenderID(), message.getImageURL());
        return message;
    }

    @MessageMapping("/repair-request")
    @SendTo("/queue/{roomID}")
    public RequestMessage handleRepairRequest(@Payload RepairRequest repairRequest) {
        String roomID = chatService.getOrCreateChatRoom(repairRequest.getLandlordID(), repairRequest.getTenantID());
        RequestMessage requestMessage = new RequestMessage(roomID, repairRequest.getTenantID(), repairRequest, LocalDateTime.now());
        chatService.sendRequestMessage(repairRequest);
        return requestMessage;
    }
}
