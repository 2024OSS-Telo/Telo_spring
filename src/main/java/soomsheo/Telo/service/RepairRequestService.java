package soomsheo.Telo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import soomsheo.Telo.domain.Chat.ChatRoom;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.repository.RepairRequestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RepairRequestService {
    private final RepairRequestRepository repairRequestRepository;
    private final ChatService chatService;

    @Autowired
    public RepairRequestService(RepairRequestRepository repairRequestRepository, ChatService chatService){
        this.repairRequestRepository = repairRequestRepository;
        this.chatService = chatService;
    }


    public void createRepairRequest(RepairRequest repairRequest) {
        repairRequestRepository.save(repairRequest);
    }

    public RepairRequest updateClaim(String requestID, Long actualValue, List<String> receiptImageURL, String claimContent) {
        Optional<RepairRequest> optionalRepairRequest = repairRequestRepository.findById(requestID);

        if (optionalRepairRequest.isPresent()) {
            RepairRequest repairRequest = optionalRepairRequest.get();
            repairRequest.updateClaim(actualValue, receiptImageURL, claimContent);
            repairRequestRepository.save(repairRequest);
            return repairRequest;
        }
        return null;
    }

    public void updateRepairState(String requestID, RepairRequest.RepairState repairState) {
        Optional<RepairRequest> optionalRepairRequest = repairRequestRepository.findById(requestID);

        if (optionalRepairRequest.isPresent()) {
            RepairRequest repairRequest = optionalRepairRequest.get();
            repairRequest.setRepairState(repairState);
            repairRequestRepository.save(repairRequest);
        }
    }

    public RepairRequest updateRefusalReason(String requestID, String refusalReason) {
        Optional<RepairRequest> optionalRepairRequest = repairRequestRepository.findById(requestID);

        if (optionalRepairRequest.isPresent()) {
            RepairRequest repairRequest = optionalRepairRequest.get();
            repairRequest.updateRefusalReason(refusalReason);
            repairRequestRepository.save(repairRequest);
            return repairRequest;
        }
        return null;
    }

    public List<RepairRequest> getRepairRequestList(String memberID) {
        return repairRequestRepository.findByLandlordIDOrTenantID(memberID, memberID);
    }

    public Optional<RepairRequest> getRepairRequest(String requestID) {
        return repairRequestRepository.findById(requestID);
    }
}
