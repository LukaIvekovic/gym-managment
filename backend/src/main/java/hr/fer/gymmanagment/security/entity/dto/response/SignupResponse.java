package hr.fer.gymmanagment.security.entity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponse {

    @NotNull
    @Schema(description = "User's id")
    private Integer id;
}
