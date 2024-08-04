package soomsheo.Telo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    private Long memberID;
    private String memberName;
    private String profile;
    private String provider; // google, kakao
    private String memberType; // landlord, tenant
}
