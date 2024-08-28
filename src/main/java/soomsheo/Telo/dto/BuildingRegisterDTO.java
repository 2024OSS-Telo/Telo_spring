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
public class BuildingRegisterDTO {
    private UUID buildingID;
    private String buildingName;
    private String buildingAddress;
    private int numberOfHouseholds;
    private int numberOfRentedHouseholds;
    private String memberRealName;
    private String phoneNumber;
    private List<String> imageURL;
    private String landlordID;
    private String notice;

}
