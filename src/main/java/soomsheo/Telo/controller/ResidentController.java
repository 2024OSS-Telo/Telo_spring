package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.dto.ResidentResisterDTO;
import soomsheo.Telo.service.BuildingService;
import soomsheo.Telo.service.ResidentService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {
    private final ResidentService residentService;
    private final BuildingService buildingService;

    public ResidentController(ResidentService residentService, BuildingService buildingService) {
        this.residentService = residentService;
        this.buildingService = buildingService;
    }

    @GetMapping("/resident-list/{buildingID}")
    public ResponseEntity<List<Resident>> getResidentsByBuildingID(@PathVariable UUID buildingID) throws Exception {
        List<Resident> residents = residentService.getAllResidents(buildingID);
        for (Resident resident : residents) {
            System.out.println("PhoneNumber: " + resident.getPhoneNumber());
        }

        if (residents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(residents);
    }

    @PostMapping("/resident-resister")
    public ResponseEntity<Resident> createResident(@RequestBody ResidentResisterDTO residentResister) throws Exception {
        UUID buildingID = residentResister.getBuildingID();
        Building building = buildingService.findByBuildingID(buildingID);

        if (building == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Resident resident = new Resident(
                residentResister.getResidentName(),
                residentResister.getPhoneNumber(),
                residentResister.getApartmentNumber(),
                residentResister.getRentType(),
                residentResister.getMonthlyRentAmount(),
                residentResister.getMonthlyRentPaymentDate(),
                residentResister.getDeposit(),
                residentResister.getContractExpirationDate(),
                building,
                residentResister.getContractImageURL()
        );

        residentService.saveResident(resident);
        return new ResponseEntity<>(resident, HttpStatus.CREATED);
    }
}
