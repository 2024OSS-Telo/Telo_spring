package soomsheo.Telo.service;

import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.domain.building.Building;
import soomsheo.Telo.domain.building.Resident;
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

    public ResidentService(ResidentRepository residentRepository, MemberRepository memberRepository) {
        this.residentRepository = residentRepository;
        this.memberRepository = memberRepository;
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
}
