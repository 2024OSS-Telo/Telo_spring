package soomsheo.Telo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDTO {
    private String apartmentNumber;
    private String rentType;
    private String monthlyRentAmount;
    private String monthlyRentPaymentDate;
    private String deposit;
    private String contractExpirationDate;

    private List<String> residentImageURL;

    private UUID buildingID;
    private String buildingName;
    private String buildingAddress;
    private String notice;
    private String landlordID;

    private List<String> buildingImageURL;
}
