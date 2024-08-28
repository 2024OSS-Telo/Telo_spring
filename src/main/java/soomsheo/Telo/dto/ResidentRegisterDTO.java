package soomsheo.Telo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soomsheo.Telo.domain.building.Building;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidentRegisterDTO {
    private String residentName;
    private String phoneNumber;
    private String apartmentNumber;
    private String rentType;
    private String monthlyRentAmount;
    private String monthlyRentPaymentDate;
    private String deposit;
    private String contractExpirationDate;
    private Building building;
    private List<String> contractImageURL;

    public UUID getBuildingID() {
        return building.getBuildingID();
    }
}
