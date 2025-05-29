package hr.fer.gymmanagment.security.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull
    @Schema(description = "User's email")
    private String email;

    @NotNull
    @Schema(description = "User's password")
    private String password;
}
