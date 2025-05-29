package hr.fer.gymmanagment.membership.dto;

import java.time.LocalDateTime;

public record MembershipDto(
        Integer id,
        Integer typeId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
