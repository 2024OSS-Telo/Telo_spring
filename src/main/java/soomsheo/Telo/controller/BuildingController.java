package soomsheo.Telo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.dto.BuildingResisterDTO;
import soomsheo.Telo.dto.NoticeUpdateDTO;
import soomsheo.Telo.service.BuildingService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @PostMapping("/landlord/building-resister")
    public ResponseEntity<Building> createBuilding(@RequestBody BuildingResisterDTO buildingResister) {
        try {

            Building building = new Building(
                    buildingResister.getBuildingName(),
                    buildingResister.getBuildingAddress(),
                    buildingResister.getNumberOfHouseholds(),
                    buildingResister.getNumberOfRentedHouseholds(),
                    buildingResister.getImageURL(),
                    buildingResister.getLandlordID(),
                    ""
            );

            buildingService.saveBuilding(building, buildingResister.getMemberRealName(), buildingResister.getPhoneNumber());

            return new ResponseEntity<>(building, HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/building-list/landlord")
    public ResponseEntity<List<Building>> getBuildingList() {
        try {
            List<Building> buildingList = buildingService.getAllBuildings();
            return new ResponseEntity<>(buildingList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member/{landlordID}")
    public ResponseEntity<List<Building>> getBuildingsByMemberID(@PathVariable String landlordID) {
        List<Building> buildings = buildingService.findByLandlordID(landlordID);
        if (buildings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(buildings, HttpStatus.OK);
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
