package soomsheo.Telo.domain.building;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID buildingID;

    private String buildingName;
    private String encryptedBuildingAddress;
    private int numberOfHouseholds;
    private int numberOfRentedHouseholds;
    private String landlordID;

    private String notice;

    @ElementCollection
    @CollectionTable(name = "building_images", joinColumns = @JoinColumn(name = "building_id"))
    @Column(name = "image_url")
    private List<String> imageURL;

    public Building(String buildingName, String buildingAddress, int numberOfHouseholds, int numberOfRentedHouseholds, List<String> imageURL, String landlordID, String notice) throws Exception {
        this.buildingID = UUID.randomUUID();
        this.buildingName = buildingName;
        this.encryptedBuildingAddress = EncryptionUtil.encrypt(buildingAddress);
        this.numberOfHouseholds = numberOfHouseholds;
        this.numberOfRentedHouseholds = numberOfRentedHouseholds;
        this.landlordID = landlordID;
        this.imageURL = imageURL;
        this.notice = notice;
    }

    public String getBuildingAddress() throws Exception {
        return EncryptionUtil.decrypt(this.encryptedBuildingAddress);
    }

    public void setBuildingAddress(String buildingAddress) throws Exception {
        this.encryptedBuildingAddress = EncryptionUtil.encrypt(buildingAddress);
    }
}
