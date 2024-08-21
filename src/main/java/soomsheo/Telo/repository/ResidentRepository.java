package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;

import java.util.List;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, String> {
    Resident findByResidentID(UUID residentID);

    List<Resident> findByBuilding_BuildingID(UUID buildingID);
}