package br.com.wefit.challenge.model.vo.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountCredentials(
    @NotBlank(message = "Email is required")
        @Email(message = "Email out of pattern, invalid")
        @Schema(example = "john.doe@domain.com")
        String email,
    @NotBlank(message = "Password is required") @Schema(example = "p@55woRd") String password) {}
