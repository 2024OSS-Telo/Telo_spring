package soomsheo.Telo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResisterDTO {
    private String buildingName;
    private String buildingAddress;
    private int numberOfHouseholds;
    private List<String> imageURL;
    private String memberID;
}
