package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.repository.BuildingRepository;
import soomsheo.Telo.repository.ResidentRepository;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;

    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    public void saveResident(Resident resident) throws Exception {
        String encryptedPhoneNumber = EncryptionUtil.encrypt(resident.getPhoneNumber());
        resident.setEncryptedPhoneNumber(encryptedPhoneNumber);
        residentRepository.save(resident);
    }

    public Resident findByResidentID(UUID residentID) {
        return residentRepository.findByResidentID(residentID);
    }

    public List<Resident> getAllResidents(UUID buildingID) throws Exception {
        List<Resident> residents = residentRepository.findByBuilding_BuildingID(buildingID);
        for (Resident resident : residents) {


            String decryptedPhoneNumber = EncryptionUtil.decrypt(resident.getEncryptedPhoneNumber());
            resident.setPhoneNumber(decryptedPhoneNumber);
            System.out.println("fuction running....... ");
        }
        return residents;
    }
}
