package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Chat.NoticeMessage;
import soomsheo.Telo.domain.Chat.RequestMessage;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.ChatService;
import soomsheo.Telo.service.FcmService;
import soomsheo.Telo.service.RepairRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repair-request")
public class RepairRequestController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final RepairRequestService repairRequestService;
    private final ChatService chatService;
    private final ChatWebSocketController chatWebSocketController;

    private final FcmService fcmService;

    @Autowired
    public RepairRequestController(RepairRequestService repairRequestService, ChatService chatService, ChatWebSocketController chatWebSocketController, FcmService fcmService) {
        this.repairRequestService = repairRequestService;
        this.chatService = chatService;
        this.chatWebSocketController = chatWebSocketController;
        this.fcmService = fcmService;
    }

    @PostMapping
    public ResponseEntity<String> createRepairRequest(@RequestBody RepairRequest request) {
        try {
            RepairRequest repairRequest = new RepairRequest(
                    request.getLandlordID(),
                    request.getTenantID(),
                    request.getRequestTitle(),
                    request.getRequestContent(),
                    request.getImageURL(),
                    request.getEstimatedValue());


            repairRequestService.createRepairRequest(repairRequest);
            String roomID = chatService.getOrCreateChatRoom(repairRequest.getLandlordID(), repairRequest.getTenantID());
            RequestMessage requestMessage = new RequestMessage(roomID, repairRequest.getTenantID(), repairRequest, LocalDateTime.now());
            chatService.saveRequestMessage(repairRequest);
            messagingTemplate.convertAndSend("/queue/" + roomID, requestMessage);

            return ResponseEntity.ok("수리 요청 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{memberID}")
    public List<RepairRequest> getRepairRequestList(@PathVariable String memberID) {
        return repairRequestService.getRepairRequestList(memberID);
    }

    @PostMapping("{roomID}/claim")
    public ResponseEntity<String> updateClaim(@RequestBody Map<String, Object> claim, @PathVariable String roomID) {
        try {
            String requestID = (String)claim.get("requestID");
            Long actualValue = ((Integer) claim.get("actualValue")).longValue();
            List<String> receiptImageURL = (List<String>)claim.get("receiptImageURL");
            String claimContent = (String)claim.get("claimContent");

            RepairRequest repairRequest = repairRequestService.updateClaim(requestID, actualValue, receiptImageURL, claimContent);
            NoticeMessage noticeMessage = new NoticeMessage(roomID, repairRequest.getTenantID(), repairRequest, "claim", LocalDateTime.now());

            chatService.saveNoticeMessage(noticeMessage);
            messagingTemplate.convertAndSend("/queue/" + roomID, noticeMessage);

            fcmService.sendPushNotification(roomID, noticeMessage);

            return ResponseEntity.ok("청구 정보 업데이트 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("{roomID}/request-refuse")
    public ResponseEntity<String> refuseRequest(@RequestBody Map<String, Object> refuse, @PathVariable String roomID) {
        try {
            String requestID = (String)refuse.get("requestID");
            String refusalReason = (String)refuse.get("refusalReason");

            RepairRequest repairRequest = repairRequestService.updateRefusalReason(requestID, refusalReason);
            NoticeMessage noticeMessage = new NoticeMessage(roomID, repairRequest.getLandlordID(), repairRequest, "refusal", LocalDateTime.now());

            chatService.saveNoticeMessage(noticeMessage);
            messagingTemplate.convertAndSend("/queue/" + roomID, noticeMessage);

            fcmService.sendPushNotification(roomID, noticeMessage);

            return ResponseEntity.ok("수리 요청 거절 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/update-state")
    public ResponseEntity<String> updateState(@RequestBody Map<String, Object> state) {
        try {
            String requestID = (String)state.get("requestID");
            String stateString = (String) state.get("state");
            RepairRequest.RepairState repairState = RepairRequest.RepairState.valueOf(stateString.toUpperCase());

            repairRequestService.updateRepairState(requestID, repairState);
            return ResponseEntity.ok("수리 요청 상태 업데이트 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
