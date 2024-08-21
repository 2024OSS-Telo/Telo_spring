package soomsheo.Telo.domain.Chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type")
public class ChatMessage {
    public enum MessageType{
        TEXT, REPAIR_REQUEST, PHOTO, NOTICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageID;

    private String roomID;
    private String senderID;
    private LocalDateTime sendDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", insertable=false, updatable=false)
    private MessageType messageType;

    public ChatMessage(String roomID, String senderID, LocalDateTime sendDate, MessageType messageType) {
        this.roomID = roomID;
        this.senderID = senderID;
        this.sendDate = sendDate;
        this.messageType = messageType;
    }

}