package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.dto.ResidentDTO;
import soomsheo.Telo.dto.ResidentRegisterDTO;
import soomsheo.Telo.service.BuildingService;
import soomsheo.Telo.service.MemberService;
import soomsheo.Telo.service.ResidentService;

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
    public ResponseEntity<List<ResidentRegisterDTO>> getResidentsByBuildingID(@PathVariable UUID buildingID) throws Exception {
        List<ResidentRegisterDTO> residents = residentService.getAllResidents(buildingID);

        if (residents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(residents);
    }

    @GetMapping("/tenant/resident-list/{tenantID}")
    public ResponseEntity<List<ResidentDTO>> getResidentsByMemberID(@PathVariable String tenantID) {
        try {
            List<ResidentDTO> residentDTOs = residentService.getResidentsByMemberID(tenantID);
            return ResponseEntity.ok(residentDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/tenant/resident-register/{buildingID}/{tenantID}")
    public ResponseEntity<Resident> createResident(@PathVariable UUID buildingID, @PathVariable String tenantID, @RequestBody ResidentRegisterDTO residentRegister) throws Exception {
        Building building = buildingService.findByBuildingID(buildingID);
        Member tenant = memberService.findByMemberID(tenantID);

        if (building == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Resident resident = new Resident(
                tenant,
                residentRegister.getApartmentNumber(),
                residentRegister.getRentType(),
                residentRegister.getMonthlyRentAmount(),
                residentRegister.getMonthlyRentPaymentDate(),
                residentRegister.getDeposit(),
                residentRegister.getContractExpirationDate(),
                building,
                residentRegister.getContractImageURL()
        );

        residentService.saveResident(resident, residentRegister.getResidentName(), residentRegister.getPhoneNumber());

        buildingService.incrementRentedHouseholds(buildingID);

        return new ResponseEntity<>(resident, HttpStatus.CREATED);
    }

    @GetMapping("/{tenantID}/{landlordID}")
    public List<Resident> getResidentsByTenantIdAndLandlordId(@PathVariable String tenantID, @PathVariable String landlordID) {
        return residentService.getResidentsByTenantIdAndLandlordId(tenantID, landlordID);
    }
}
