package soomsheo.Telo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.domain.Chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
