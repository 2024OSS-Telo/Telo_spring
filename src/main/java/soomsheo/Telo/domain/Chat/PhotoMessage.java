package soomsheo.Telo.domain.Chat;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("PHOTO")
public class PhotoMessage extends ChatMessage {
    private String imageURL;

    public PhotoMessage (String roomID, String senderID, String imageURL, LocalDateTime sendDate){
        super(roomID, senderID, sendDate, MessageType.PHOTO);
        this.imageURL = imageURL;
    }

}
