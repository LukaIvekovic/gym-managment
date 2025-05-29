package hr.fer.gymmanagment.security.entity.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private Long accessTokenExpires;
    private String type = "Bearer";
    private UserResponse user;

    public JwtResponse(String accessToken,
                       Long accessExpiry,
                       UserResponse user) {
        this.accessToken = accessToken;
        this.accessTokenExpires = accessExpiry;
        this.user = user;
    }
}
