package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.dto.ResidentResisterDTO;
import soomsheo.Telo.service.BuildingService;
import soomsheo.Telo.service.MemberService;
import soomsheo.Telo.service.ResidentService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {
    private final ResidentService residentService;
    private final BuildingService buildingService;
    private final MemberService memberService;

    public ResidentController(ResidentService residentService, BuildingService buildingService, MemberService memberService) {
        this.residentService = residentService;
        this.buildingService = buildingService;
        this.memberService = memberService;
    }

    @GetMapping("/landlord/resident-list/{buildingID}")
    public ResponseEntity<List<ResidentResisterDTO>> getResidentsByBuildingID(@PathVariable UUID buildingID) throws Exception {
        List<ResidentResisterDTO> residents = residentService.getAllResidents(buildingID);
//        for (Resident resident : residents) {
//            System.out.println("PhoneNumber: " + resident.getPhoneNumber());
//        }

        if (residents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(residents);
    }

    @PostMapping("/tenant/resident-resister/{buildingID}/{tenantID}")
    public ResponseEntity<Resident> createResident(@PathVariable UUID buildingID, @PathVariable String tenantID, @RequestBody ResidentResisterDTO residentResister) throws Exception {
        Building building = buildingService.findByBuildingID(buildingID);
        Member tenant = memberService.findByMemberID(tenantID);

        if (building == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Resident resident = new Resident(
                tenant,
                residentResister.getApartmentNumber(),
                residentResister.getRentType(),
                residentResister.getMonthlyRentAmount(),
                residentResister.getMonthlyRentPaymentDate(),
                residentResister.getDeposit(),
                residentResister.getContractExpirationDate(),
                building,
                residentResister.getContractImageURL()
        );

        residentService.saveResident(resident, residentResister.getResidentName(), residentResister.getPhoneNumber());

        buildingService.incrementRentedHouseholds(buildingID);

        return new ResponseEntity<>(resident, HttpStatus.CREATED);
    }

    @GetMapping("/{tenantID}/{landlordID}")
    public List<Resident> getResidentsByTenantIdAndLandlordId(@PathVariable String tenantID, @PathVariable String landlordID) {
        return residentService.getResidentsByTenantIdAndLandlordId(tenantID, landlordID);
    }
}
