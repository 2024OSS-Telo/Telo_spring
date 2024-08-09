package soomsheo.Telo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class RepairRequest {
    public enum RepairState{
        NONE, REFUSAL, APPROVAL, UNDER_REPAIR, CLAIM, COMPLETE
    }

    @Id
    private String requestID;

    private String landlordID;
    private String tenantID;
    private String requestTitle;
    private String requestContent;

    private Long estimateValue;
    @Setter
    private RepairState repairState;

    @ElementCollection
    @CollectionTable(name = "repair_request_images", joinColumns = @JoinColumn(name = "request_id"))
    private List<String> imageURL;

    public RepairRequest(String landlordID, String tenantID, String requestTitle, String requestContent, List<String> imageURL, Long estimateValue) {
        this.requestID = UUID.randomUUID().toString();
        this.landlordID = landlordID;
        this.tenantID = tenantID;
        this.requestTitle = requestTitle;
        this.requestContent = requestContent;
        this.imageURL = imageURL;
        this.estimateValue = estimateValue;
        this.repairState = RepairState.NONE;
    }
}
