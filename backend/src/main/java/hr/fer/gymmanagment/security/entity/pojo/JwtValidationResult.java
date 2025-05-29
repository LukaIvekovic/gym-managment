package hr.fer.gymmanagment.security.entity.pojo;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtValidationResult {

    private final boolean isValid;

    private final DecodedJWT decodedJWT;

    public boolean isValid() {
        return isValid;
    }
}
