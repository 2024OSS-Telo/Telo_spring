package soomsheo.Telo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.repository.RepairRequestRepository;

import java.util.List;

@Service
public class RepairRequestService {
    private final RepairRequestRepository repairRequestRepository;
    private final ChatService chatService;

    @Autowired
    public RepairRequestService(RepairRequestRepository repairRequestRepository, ChatService chatService){
        this.repairRequestRepository = repairRequestRepository;
        this.chatService = chatService;
    }


    public void createRequest(RepairRequest repairRequest) {
        repairRequestRepository.save(repairRequest);
    }
}
