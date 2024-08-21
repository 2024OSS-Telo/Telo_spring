package soomsheo.Telo.domain.Chat;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soomsheo.Telo.domain.RepairRequest;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("NOTICE")
public class NoticeMessage extends ChatMessage {
    @ManyToOne
    @JoinColumn(name = "repair_request_id")
    private RepairRequest repairRequest;

    private String noticeType; // refusal, approval, claim, complete

    public NoticeMessage (String roomID, String senderID, RepairRequest repairRequest, String noticeType, LocalDateTime sendDate){
        super(roomID, senderID, sendDate, MessageType.NOTICE);
        this.repairRequest = repairRequest;
        this.noticeType = noticeType;
    }
}
