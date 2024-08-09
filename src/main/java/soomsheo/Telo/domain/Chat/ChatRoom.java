package soomsheo.Telo.domain.Chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom {
    @Id
    private String roomID;
    private String landlordID;
    private String tenantID;

    public ChatRoom(String landlordID, String tenantID) {
        this.roomID = UUID.randomUUID().toString();
        this.landlordID = landlordID;
        this.tenantID = tenantID;
    }

}
