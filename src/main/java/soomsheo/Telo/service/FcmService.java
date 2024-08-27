package soomsheo.Telo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import soomsheo.Telo.domain.Chat.ChatMessage;
import soomsheo.Telo.domain.Chat.ChatRoom;
import soomsheo.Telo.domain.Chat.PhotoMessage;
import soomsheo.Telo.domain.Chat.TextMessage;
import soomsheo.Telo.domain.Member;
import soomsheo.Telo.dto.FcmMessageDTO;
import soomsheo.Telo.dto.FcmSendDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


@Service
public class FcmService {

    private final MemberService memberService;
    private final ChatService chatService;

    @Autowired
    public FcmService(MemberService memberService, ChatService chatService) {
        this.memberService = memberService;
        this.chatService = chatService;
    }

    public void sendMessageTo(FcmSendDTO fcmSendDTO) throws IOException {
        String message = makeMessage(fcmSendDTO);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/telo-fae7f/messages:send";
        ResponseEntity response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/telo-firebase-key.json";

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeMessage(FcmSendDTO fcmSendDTO) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        FcmMessageDTO fcmMessageDto = FcmMessageDTO.builder()
                .message(FcmMessageDTO.Message.builder()
                        .token(fcmSendDTO.getToken())
                        .notification(FcmMessageDTO.Notification.builder()
                                .title(fcmSendDTO.getTitle())
                                .body(fcmSendDTO.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }

    public void sendPushNotification(String roomID, ChatMessage chatMessage) throws IOException {
        Member Sender = memberService.findByMemberID(chatMessage.getSenderID());
        ChatRoom chatRoom = chatService.findByRoomID(roomID);
        Member other;
        if (Objects.equals(chatRoom.getLandlordID(), chatMessage.getSenderID())) {
            other = memberService.findByMemberID(chatRoom.getTenantID());
        }
        else {
            other = memberService.findByMemberID(chatRoom.getLandlordID());
        }

        FcmSendDTO fcmSendDTO = new FcmSendDTO();
        fcmSendDTO.setToken(other.getToken());
        fcmSendDTO.setTitle("TELO");

        String messageBody = Sender.getMemberNickName() + "님이 보낸 새로운 메시지가 있습니다.";

        fcmSendDTO.setBody(messageBody);
        this.sendMessageTo(fcmSendDTO);
    }
}
