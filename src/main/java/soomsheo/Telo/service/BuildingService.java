package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.repository.BuildingRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public void saveBuilding(Building building) {
        buildingRepository.save(building);
    }

    public Building findByBuildingID(UUID buildingID) {
        return buildingRepository.findByBuildingID(buildingID);
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }
}
