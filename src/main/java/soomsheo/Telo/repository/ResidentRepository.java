package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;

import java.util.List;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {
    Resident findByResidentID(UUID residentID);

    List<Resident> findByBuilding_BuildingID(UUID buildingID);

    List<Resident> findByTenantMemberID(String tenantID);


    @Query("SELECT r.building FROM Resident r WHERE r.tenant.memberID = :tenantID AND r.building.landlordID = :landlordID")
    List<Building> findBuildingsByTenantIdAndLandlordId(@Param("tenantID") String tenantID, @Param("landlordID") String landlordID);

    @Query("SELECT r FROM Resident r WHERE r.tenant.memberID = :tenantID AND r.building.landlordID = :landlordID")
    List<Resident> findResidentsByTenantIdAndLandlordId(@Param("tenantID") String tenantID, @Param("landlordID") String landlordID);
}