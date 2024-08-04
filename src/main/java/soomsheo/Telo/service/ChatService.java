package soomsheo.Telo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soomsheo.Telo.domain.Chat.*;
import soomsheo.Telo.domain.RepairRequest;
import soomsheo.Telo.repository.ChatMessageRepository;
import soomsheo.Telo.repository.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository){
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> getChatRoomList() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public ChatMessage sendRequestMessage(RepairRequest repairRequest) {
        String roomID = getOrCreateChatRoom(repairRequest.getLandlordID(), repairRequest.getTenantID());
        ChatMessage chatMessage = new RequestMessage(roomID, repairRequest.getTenantID(), repairRequest, LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public ChatMessage sendTextMessage(String roomID, Long senderID, String message) {
        ChatMessage chatMessage = new TextMessage(roomID, senderID, message, LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public ChatMessage sendPhotoMessage(String roomID, Long senderID, String photoURL) {
        ChatMessage chatMessage = new PhotoMessage(roomID, senderID, photoURL, LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    public String getOrCreateChatRoom(Long landlordID, Long tenantID) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByLandlordIDAndTenantID(landlordID, tenantID);
        if (chatRoomOpt.isEmpty()){
            ChatRoom chatRoom = new ChatRoom(landlordID, tenantID);
            chatRoomRepository.save(chatRoom);
            return chatRoom.getRoomID();
        }
        else
            return chatRoomOpt.get().getRoomID();
    }
}
