package soomsheo.Telo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soomsheo.Telo.domain.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String memberID;
    private String memberRealName;
    private String memberNickName;
    private String phoneNumber;
    private String profile;
    private String provider;
    private String memberType;
}
