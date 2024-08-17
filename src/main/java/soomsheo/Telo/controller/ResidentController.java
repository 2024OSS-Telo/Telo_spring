package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.dto.ResidentResisterDTO;
import soomsheo.Telo.service.BuildingService;
import soomsheo.Telo.service.ResidentService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Controller
public class ResidentController {
    private final ResidentService residentService;
    private final BuildingService buildingService;

    public ResidentController(ResidentService residentService, BuildingService buildingService) {
        this.residentService = residentService;
        this.buildingService = buildingService;
    }

    @PostMapping("/resident-resister")
    public ResponseEntity<Resident> createResident(@RequestBody ResidentResisterDTO residentResister) {
        try {
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
                    residentResister.getRentPaymentDate(),
                    building,
                    residentResister.getContractImageURL()
            );

            residentService.saveResident(resident);
            return new ResponseEntity<>(resident, HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
