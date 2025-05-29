package hr.fer.gymmanagment.chat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime timestamp;
}
