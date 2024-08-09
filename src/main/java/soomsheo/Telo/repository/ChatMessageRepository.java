package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.Chat.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomID(String roomID);
}
