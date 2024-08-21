package soomsheo.Telo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class RepairRequest {
    public enum RepairState{
        NONE, REFUSAL, UNDER_REPAIR, CLAIM, COMPLETE
    }

    @Id
    private String requestID;

    private String landlordID;
    private String tenantID;
    private String requestTitle;
    private String requestContent;
    private String claimContent;

    private Long estimatedValue;
    private Long actualValue;

    private LocalDateTime createdDate;

    private String refusalReason;

    @Setter
    private RepairState repairState;

    @ElementCollection
    @CollectionTable(name = "repair_request_images", joinColumns = @JoinColumn(name = "request_id"))
    private List<String> imageURL;
    @ElementCollection
    @CollectionTable(name = "repair_request_receipt_images", joinColumns = @JoinColumn(name = "request_id"))
    private List<String> receiptImageURL;

    public RepairRequest(String landlordID, String tenantID, String requestTitle, String requestContent, List<String> imageURL, Long estimatedValue) {
        this.requestID = UUID.randomUUID().toString();
        this.landlordID = landlordID;
        this.tenantID = tenantID;
        this.requestTitle = requestTitle;
        this.requestContent = requestContent;
        this.imageURL = imageURL;
        this.estimatedValue = estimatedValue;
        this.repairState = RepairState.NONE;
        this.createdDate = LocalDateTime.now();
    }

    public void updateClaim(Long actualValue, List<String> receiptImageURL, String claimContent) {
        this.actualValue = actualValue;
        this.receiptImageURL = receiptImageURL;
        this.claimContent = claimContent;
        this.repairState = RepairState.CLAIM;
    }

    public void updateRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
        this.repairState = RepairState.REFUSAL;
    }
}
