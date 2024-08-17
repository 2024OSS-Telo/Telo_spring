package soomsheo.Telo.domain.building;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import soomsheo.Telo.domain.Member;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private String buildingAddress;
    private int numberOfHouseholds;
    private String memberID;

    @ElementCollection
    @CollectionTable(name = "building_images", joinColumns = @JoinColumn(name = "building_id"))
    @Column(name = "image_url")
    private List<String> imageURL;

    public Building(String buildingName, String buildingAddress, int numberOfHouseholds, List<String> imageURL, String memberID) throws NoSuchAlgorithmException {
        this.buildingID = UUID.randomUUID();
        this.buildingName = buildingName;
        this.buildingAddress = hashAddress(buildingAddress);
        this.numberOfHouseholds = numberOfHouseholds;
        this.memberID = memberID;
        this.imageURL = imageURL;
    }

    private String hashAddress(String address) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(address.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
