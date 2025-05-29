package hr.fer.gymmanagment.groupclass.dto;

import hr.fer.gymmanagment.groupclass.entity.GroupClassType;

import java.time.LocalDateTime;

public record GroupClassDto(
        Integer id,
        String name,
        String description,
        GroupClassType type,
        LocalDateTime dateTime,
        Integer duration,
        Integer maxParticipants,
        Integer currentParticipants
) {
}
