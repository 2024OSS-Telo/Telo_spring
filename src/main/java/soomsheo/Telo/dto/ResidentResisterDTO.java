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
public class ResidentResisterDTO {
    private String residentName;
    private String phoneNumber;
    private String apartmentNumber;
    //1이 전세, 2이 월세
    private int rentType;
    private String rentPaymentDate;
    private UUID buildingID;
    private List<String> contractImageURL;
}
