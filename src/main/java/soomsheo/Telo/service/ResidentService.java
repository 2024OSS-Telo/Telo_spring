package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.repository.BuildingRepository;
import soomsheo.Telo.repository.ResidentRepository;

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;

    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    public void saveResident(Resident resident) {
        residentRepository.save(resident);
    }
}
