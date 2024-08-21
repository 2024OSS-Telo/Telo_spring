package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.repository.BuildingRepository;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public void saveBuilding(Building building) throws Exception {
        String encryptedAddress = EncryptionUtil.encrypt(building.getBuildingAddress());
        building.setEncryptedBuildingAddress(encryptedAddress);
        buildingRepository.save(building);
    }

    public Building findByBuildingID(UUID buildingID) {
        return buildingRepository.findByBuildingID(buildingID);
    }

    public List<Building> getAllBuildings() throws Exception {
        List<Building> buildings = buildingRepository.findAll();
        for (Building building : buildings) {
            String decryptedAddress = EncryptionUtil.decrypt(building.getEncryptedBuildingAddress());
            building.setBuildingAddress(decryptedAddress);
        }
        return buildings;
    }
    public List<Building> findByMemberID(String memberID) {
        return buildingRepository.findByMemberID(memberID);
    }

    public Building updateNotice(UUID buildingId, String notice) {
        Building building = buildingRepository.findByBuildingID(buildingId);
        if (building != null) {
            building.setNotice(notice);
            return buildingRepository.save(building);
        }
        return null;
    }

    public Optional<Building> getBuildingById(UUID buildingID) {
        return Optional.ofNullable(buildingRepository.findByBuildingID(buildingID));
    }
}
