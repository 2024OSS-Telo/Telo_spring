package soomsheo.Telo.domain.building;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID residentID;

    private String residentName;
    private String phoneNumber;
    private String apartmentNumber;
    //1이 전세, 2이 월세
    private int rentType;
    private String rentPaymentDate;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ElementCollection
    @CollectionTable(name = "resident_contract_images", joinColumns = @JoinColumn(name = "resident_id"))
    @Column(name = "contract_image_url")
    private List<String> contractImageURL;

    public Resident(String residentName, String phoneNumber, String apartmentNumber, int rentType,
                    String rentPaymentDate, Building building, List<String> contractImageURL) throws NoSuchAlgorithmException {
        this.residentID = UUID.randomUUID();
        this.residentName = residentName;
        this.phoneNumber = hashPhoneNumber(phoneNumber);
        this.apartmentNumber = apartmentNumber;
        this.rentType = rentType;
        this.rentPaymentDate = rentPaymentDate;
        this.building = building;
        this.contractImageURL = contractImageURL;
    }

    private String hashPhoneNumber(String address) throws NoSuchAlgorithmException {
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
