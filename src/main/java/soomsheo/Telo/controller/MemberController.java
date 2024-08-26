package soomsheo.Telo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.dto.MemberTypeUpdateRequestDTO;
import soomsheo.Telo.service.MemberService;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public Member signUp(@RequestBody Member member) {
        return memberService.saveMember(member);
    }

    @GetMapping("/{memberID}")
    public ResponseEntity<?> getLandlordDetails(@PathVariable String memberID) {
        try {
            Member member = memberService.findByMemberID(memberID);
            if (member == null) {
                return ResponseEntity.notFound().build();
            }

            System.out.print("landlord member check: " + member.getMemberRealName() + member.getPhoneNumber());
            return ResponseEntity.ok().body(Map.of(
                    "memberRealName", member.getMemberRealName(),
                    "phoneNumber", member.getPhoneNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 에러: " + e.getMessage());
        }
    }

    @PostMapping("/updateMemberType/{memberID}")
    public ResponseEntity<String> updateMemberType(
            @PathVariable String memberID,
            @RequestBody MemberTypeUpdateRequestDTO request) {
        try {
            memberService.updateMemberType(memberID, request.getMemberType());
            return new ResponseEntity<>("Member type updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{memberID}/memberType")
    public String getMemberType(@PathVariable String memberID) {
        Member member = memberService.findByMemberID(memberID);
        if (member != null) {
            System.out.println("memberID : " + memberID);
            return member.getMemberType();
        } else {
            return "Member not found";
        }
    }
}