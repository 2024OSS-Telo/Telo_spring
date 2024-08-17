package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.dto.BuildingResisterDTO;
import soomsheo.Telo.service.BuildingService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @PostMapping("/building-resister")
    public ResponseEntity<Building> createBuilding(@RequestBody BuildingResisterDTO buildingResister) {
        try {
            Building building = new Building(
                    buildingResister.getBuildingName(),
                    buildingResister.getBuildingAddress(),
                    buildingResister.getNumberOfHouseholds(),
                    buildingResister.getImageURL(),
                    buildingResister.getMemberID()
            );

            buildingService.saveBuilding(building);
            return new ResponseEntity<>(building, HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/building-list")
    public ResponseEntity<List<Building>> getBuildingList() {
        try {
            List<Building> buildingList = buildingService.getAllBuildings();
            return new ResponseEntity<>(buildingList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
