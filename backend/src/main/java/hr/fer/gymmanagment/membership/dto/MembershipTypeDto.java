package hr.fer.gymmanagment.membership.dto;

import java.math.BigDecimal;

public record MembershipTypeDto(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer duration
) {
}
