package soomsheo.Telo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.repository.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member saveMember(Member member) {
        if (memberRepository.findByMemberID(member.getMemberID()) != null) {
            return null;
        }
        return memberRepository.save(member);
    }

    public Member findByMemberID(String memberID) {
        return memberRepository.findByMemberID(memberID);
    }

    @Transactional
    public void updateMemberType(String memberID, String memberType) {
        Member member = memberRepository.findById(memberID)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberID));
        member.setMemberType(memberType);
        memberRepository.save(member);
    }

}
