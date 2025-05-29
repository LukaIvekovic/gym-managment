package hr.fer.gymmanagment.security.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtGenerationResult {

    private final String token;

    private final Long timestamp;
}
