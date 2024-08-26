package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
import soomsheo.Telo.dto.ResidentDTO;
import soomsheo.Telo.dto.ResidentResisterDTO;
import soomsheo.Telo.repository.BuildingRepository;
import soomsheo.Telo.repository.MemberRepository;
import soomsheo.Telo.repository.ResidentRepository;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final MemberRepository memberRepository;
    private final BuildingRepository buildingRepository;

    public ResidentService(ResidentRepository residentRepository, MemberRepository memberRepository, BuildingRepository buildingRepository) {
        this.residentRepository = residentRepository;
        this.memberRepository = memberRepository;
        this.buildingRepository = buildingRepository;
    }

    public void saveResident(Resident resident, String realName, String phoneNumber) throws Exception {
        Member tenant = resident.getTenant();
        if(tenant.getEncryptedPhoneNumber() == null || tenant.getEncryptedPhoneNumber().isEmpty()) {
            String encryptedPhoneNumber = EncryptionUtil.encrypt(phoneNumber);
            tenant.setEncryptedPhoneNumber(encryptedPhoneNumber);
            tenant.setMemberRealName(realName);
            memberRepository.save(tenant);
        };
        residentRepository.save(resident);
    }

    public Resident findByResidentID(UUID residentID) {
        return residentRepository.findByResidentID(residentID);
    }

    public List<ResidentResisterDTO> getAllResidents(UUID buildingID) throws Exception {
        List<Resident> residents = residentRepository.findByBuilding_BuildingID(buildingID);
        List<ResidentResisterDTO> resisterDTOS = new ArrayList<>();

        for (Resident resident : residents) {
            String decryptedPhoneNumber = EncryptionUtil.decrypt(resident.getTenant().getEncryptedPhoneNumber());

            ResidentResisterDTO resisterDTO = new ResidentResisterDTO(
                    resident.getTenant().getMemberRealName(),
                    decryptedPhoneNumber,
                    resident.getApartmentNumber(),
                    resident.getRentType(),
                    resident.getMonthlyRentAmount(),
                    resident.getMonthlyRentPaymentDate(),
                    resident.getDeposit(),
                    resident.getContractExpirationDate(),
                    resident.getBuilding(),
                    resident.getContractImageURL()
            );

            resisterDTOS.add(resisterDTO);

            System.out.println("fuction running....... ");
        }
        return resisterDTOS;
    }

    public List<ResidentDTO> getResidentsByMemberID(String memberID) throws Exception {
        List<Resident> residents = residentRepository.findByTenantMemberID(memberID);

        List<ResidentDTO> residentDTOs = new ArrayList<>();

        for (Resident resident : residents) {
            Building building = buildingRepository.findByBuildingID(resident.getBuilding().getBuildingID());


            String buildingAddress = building.getBuildingAddress();

            ResidentDTO dto = new ResidentDTO(
                    resident.getApartmentNumber(),
                    resident.getRentType(),
                    resident.getMonthlyRentAmount(),
                    resident.getMonthlyRentPaymentDate(),
                    resident.getDeposit(),
                    resident.getContractExpirationDate(),
                    resident.getContractImageURL(),
                    building.getBuildingID(),
                    building.getBuildingName(),
                    buildingAddress,
                    building.getNotice(),
                    building.getLandlordID(),
                    building.getImageURL()
            );
            residentDTOs.add(dto);
        }

        return residentDTOs;
    }

    public List<Building> getBuildingsByTenantIDAndLandlordID (String tenantID, String landlordID) {
        return residentRepository.findBuildingsByTenantIdAndLandlordId(tenantID, landlordID);
    }

    public List<Resident> getResidentsByTenantIdAndLandlordId (String tenantID, String landlordID) {
        return residentRepository.findResidentsByTenantIdAndLandlordId(tenantID, landlordID);
    }
}
