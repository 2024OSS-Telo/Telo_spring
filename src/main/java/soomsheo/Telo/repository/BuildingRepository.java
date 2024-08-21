package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    Building findByBuildingID(UUID buildingID);

    List<Building> findByMemberID(String memberID);
}