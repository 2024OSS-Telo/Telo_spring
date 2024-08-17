package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;

public interface ResidentRepository extends JpaRepository<Resident, String> {
}