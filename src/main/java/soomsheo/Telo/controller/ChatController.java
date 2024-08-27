package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Chat.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.ChatService;
import soomsheo.Telo.service.FcmService;
import soomsheo.Telo.service.RepairRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatWebSocketController chatWebSocketController;
    private final RepairRequestService repairRequestService;
    private final FcmService fcmService;

    @Autowired
    public ChatController(ChatService chatService, ChatWebSocketController chatWebSocketController, RepairRequestService repairRequestService, FcmService fcmService) {
        this.chatService = chatService;
        this.chatWebSocketController = chatWebSocketController;
        this.repairRequestService = repairRequestService;
        this.fcmService = fcmService;
    }

    @GetMapping("/rooms/{memberID}")
    public List<ChatRoom> getChatRoomList(@PathVariable String memberID) {
        return chatService.getChatRoomList(memberID);
    }

    @GetMapping("/{roomID}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable String roomID) {
        return chatService.getChatMessages(roomID);
    }

    @PostMapping("/{roomID}/create-notice")
    public ResponseEntity<String> createNoticeMessage(@RequestBody Map<String, Object> notice, @PathVariable String roomID) {
        try {
            String requestID = (String) notice.get("requestID");
            String noticeType = (String) notice.get("noticeType");

            RepairRequest repairRequest = repairRequestService.getRepairRequest(requestID).get();
            NoticeMessage noticeMessage = new NoticeMessage(roomID, repairRequest.getLandlordID(), repairRequest, noticeType, LocalDateTime.now());
            chatService.saveNoticeMessage(noticeMessage);
            messagingTemplate.convertAndSend("/queue/" + roomID, noticeMessage);

            fcmService.sendPushNotification(roomID, noticeMessage);
            return ResponseEntity.ok("알림 메시지 생성 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
