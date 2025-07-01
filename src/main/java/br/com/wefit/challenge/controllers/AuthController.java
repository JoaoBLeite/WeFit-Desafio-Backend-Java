package br.com.wefit.challenge.controllers;

import br.com.wefit.challenge.model.vo.request.auth.AccountCredentials;
import br.com.wefit.challenge.model.vo.response.ServerResponse;
import br.com.wefit.challenge.model.vo.response.auth.TokenData;
import br.com.wefit.challenge.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signing")
  @Operation(
      summary = "Sign in endpoint",
      description = "Sign in for a registered user",
      tags = {"Auth - V1"},
      responses = {
        @ApiResponse(
            description = "Authenticated",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = TokenData.class))),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Bad Credentials", responseCode = "401", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      })
  public ResponseEntity<ServerResponse<TokenData>> signingEndpoint(
      @RequestBody @Valid AccountCredentials accountCredentials, HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    TokenData token = authService.signing(accountCredentials);
    return ResponseEntity.ok(ServerResponse.success("Login successful", token, requestPath));
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "Refresh endpoint",
      description = "Refresh user expired access token",
      tags = {"Auth - V1"},
      responses = {
        @ApiResponse(
            description = "Refreshed",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = TokenData.class))),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Bad Credentials", responseCode = "403", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      })
  public ResponseEntity<ServerResponse<TokenData>> refreshEndpoint(
      @RequestHeader("Refresh-Token") @NotBlank String refreshToken, HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    TokenData token = authService.refresh(refreshToken);
    return ResponseEntity.ok(
        ServerResponse.success("Tokens refreshed successfully", token, requestPath));
  }
}
