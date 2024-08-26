package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soomsheo.Telo.domain.building.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    Building findByBuildingID(UUID buildingID);
    List<Building> findByLandlordID(String landlordID);

    @Query("SELECT b.encryptedBuildingAddress FROM Building b")
    List<String> findAllEncryptedAddresses();
}