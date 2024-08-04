package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.service.RepairRequestService;

@Controller
public class RepairRequestController {
    private final RepairRequestService repairRequestService;

    @Autowired
    public RepairRequestController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    @PostMapping("/repair-request")
    public ResponseEntity<String> createRepairRequest(@RequestBody RepairRequest request) {
        try {
            RepairRequest repairRequest = new RepairRequest(
                    request.getLandlordID(),
                    request.getTenantID(),
                    request.getRequestTitle(),
                    request.getRequestContent(),
                    request.getImageURL(),
                    request.getEstimateValue());

            repairRequestService.createRequest(repairRequest);
            return ResponseEntity.ok("수리 요청 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
