package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.dto.BuildingRegisterDTO;
import soomsheo.Telo.dto.NoticeUpdateDTO;
import soomsheo.Telo.service.BuildingService;
import soomsheo.Telo.service.MemberService;
import soomsheo.Telo.util.EncryptionUtil;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {
    private final BuildingService buildingService;
    private final MemberService memberService;

    public BuildingController(BuildingService buildingService, MemberService memberService) {
        this.buildingService = buildingService;
        this.memberService = memberService;
    }

    @PostMapping("/landlord/building-register")
    public ResponseEntity<Building> createBuilding(@RequestBody BuildingRegisterDTO buildingRegister) {
        try {

            Building building = new Building(
                    buildingRegister.getBuildingName(),
                    buildingRegister.getBuildingAddress(),
                    buildingRegister.getNumberOfHouseholds(),
                    buildingRegister.getNumberOfRentedHouseholds(),
                    buildingRegister.getImageURL(),
                    buildingRegister.getLandlordID(),
                    ""
            );

            buildingService.saveBuilding(building, buildingRegister.getMemberRealName(), buildingRegister.getPhoneNumber());

            return new ResponseEntity<>(building, HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/landlord/building-list/{landlordID}")
    public ResponseEntity<List<Building>> getBuildingsByLandlordID(@PathVariable String landlordID) {
        List<Building> buildings = buildingService.findByLandlordID(landlordID);
        if (buildings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @GetMapping("/autocomplete")
    public List<String> autocompleteAddress(@RequestParam String address) throws Exception {
        return buildingService.findMatchingBuildingAddresses(address);
    }

    @GetMapping("/address-compare")
    public ResponseEntity<Map<String, Object>> getBuildingInfo(@RequestParam String buildingAddress) {
        try {
            List<Building> buildings = buildingService.getAllBuildings();
            for (Building building : buildings) {
                String decryptedAddress = EncryptionUtil.decrypt(building.getEncryptedBuildingAddress());
                System.out.println("주소 비교: " + decryptedAddress + " with " + buildingAddress);

                if (decryptedAddress.equals(buildingAddress)) {
                    Member member = memberService.findByMemberID(building.getLandlordID());

                    if (member != null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("buildingName", building.getBuildingName());
                        response.put("buildingID", building.getBuildingID());
                        response.put("memberRealName", member.getMemberRealName());

                        System.out.println("buildingID: " + building.getBuildingID());
                        System.out.println("Landlord name to match: " + member.getMemberRealName());

                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                } else {
                    System.out.println("Address did not match: " + decryptedAddress + " vs " + buildingAddress);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{buildingID}")
    public ResponseEntity<Building> getBuilding(@PathVariable UUID buildingID) {
        try {
            Optional<Building> building = buildingService.getBuildingById(buildingID);
            return building.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{buildingID}/notice")
    public ResponseEntity<Building> updateNotice(@PathVariable UUID buildingID, @RequestBody NoticeUpdateDTO dto) {
        try {
            Building updatedBuilding = buildingService.updateNotice(buildingID, dto.getNotice());
            return updatedBuilding != null ? ResponseEntity.ok(updatedBuilding) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
