package soomsheo.Telo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class RepairRequest {
    @Id
    private String requestID;

    private Long landlordID;
    private Long tenantID;
    private String requestTitle;
    private String requestContent;

    private Long estimateValue;

    @ElementCollection
    @CollectionTable(name = "repair_request_images", joinColumns = @JoinColumn(name = "request_id"))
    private List<String> imageURL;

    public RepairRequest(Long landlordID, Long tenantID, String requestTitle, String requestContent, List<String> imageURL, Long estimateValue) {
        this.requestID = UUID.randomUUID().toString();
        this.landlordID = landlordID;
        this.tenantID = tenantID;
        this.requestTitle = requestTitle;
        this.requestContent = requestContent;
        this.imageURL = imageURL;
        this.estimateValue = estimateValue;
    }

}
