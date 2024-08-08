package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByMemberID(String memberID);
}