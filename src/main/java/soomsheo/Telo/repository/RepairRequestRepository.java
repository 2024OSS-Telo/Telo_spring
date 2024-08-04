package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.RepairRequest;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, String> {
}
