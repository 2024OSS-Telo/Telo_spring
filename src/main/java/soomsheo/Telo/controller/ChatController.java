package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soomsheo.Telo.domain.Chat.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.ChatService;
import soomsheo.Telo.service.RepairRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final RepairRequestService repairRequestService;

    @Autowired
    public ChatController(ChatService chatService, RepairRequestService repairRequestService) {
        this.chatService = chatService;
        this.repairRequestService = repairRequestService;
    }

    @GetMapping("/rooms/{memberID}")
    public List<ChatRoom> getChatRoomList(@PathVariable String memberID) {
        return chatService.getChatRoomList(memberID);
    }

    @GetMapping("/{roomID}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable String roomID) {
        return chatService.getChatMessages(roomID);
    }
}
