package hr.fer.gymmanagment.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantCountDto {
    private Integer groupClassId;
    private Integer participantCount;
}
