package soomsheo.Telo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soomsheo.Telo.util.EncryptionUtil;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    private String memberID;
    private String memberRealName;
    private String memberNickName;
    private String encryptedPhoneNumber;
    private String profile;
    private String provider; // google, kakao
    private String memberType; // landlord, tenant
    private String token; // device token

    public String getPhoneNumber() throws Exception {
        return EncryptionUtil.decrypt(this.encryptedPhoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) throws Exception {
        this.encryptedPhoneNumber = EncryptionUtil.encrypt(phoneNumber);
    }

}
