package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.Chat.ChatRoom;
import soomsheo.Telo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByLandlordIDAndTenantID(String landlordID, String tenantID);
    List<ChatRoom> findByLandlordIDOrTenantID(String landlordID, String tenantID);
    ChatRoom findByRoomID(String roomID);
}
