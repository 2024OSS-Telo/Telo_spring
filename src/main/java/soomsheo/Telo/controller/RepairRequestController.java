package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.RepairRequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repair-request")
public class RepairRequestController {
    private final RepairRequestService repairRequestService;
    private final ChatWebSocketController chatWebSocketController;

    @Autowired
    public RepairRequestController(RepairRequestService repairRequestService, ChatWebSocketController chatWebSocketController) {
        this.repairRequestService = repairRequestService;
        this.chatWebSocketController = chatWebSocketController;
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
            chatWebSocketController.handleRepairRequest(repairRequest);

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

    @PostMapping("/claim")
    public ResponseEntity<String> updateClaim(@RequestBody Map<String, Object> claim) {
        try {
            String requestID = (String)claim.get("requestID");
            Long actualValue = ((Integer) claim.get("actualValue")).longValue();
            List<String> receiptImageURL = (List<String>)claim.get("receiptImageURL");
            String claimContent = (String)claim.get("claimContent");

            repairRequestService.updateClaim(requestID, actualValue, receiptImageURL, claimContent);
            return ResponseEntity.ok("청구 정보 업데이트 성공");
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
