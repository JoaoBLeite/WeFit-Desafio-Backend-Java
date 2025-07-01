package br.com.wefit.challenge.controllers;

import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.ServerResponse;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;
import br.com.wefit.challenge.services.interfaces.LegalEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/legal-entity/v1")
@RequiredArgsConstructor
public class LegalEntityController {

  private final LegalEntityService legalEntityService;

  @PostMapping
  @Operation(
      summary = "Create legal entity profile endpoint",
      description = "Legal entity profile creation",
      tags = {"Legal Entity - V1"},
      responses = {
        @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = @Content(schema = @Schema(implementation = LegalEntityData.class))),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
        @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
        @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
      })
  public ResponseEntity<ServerResponse<LegalEntityData>> createLegalEntity(
      @RequestBody @Valid CreateLegalEntity createLegalEntity, HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    LegalEntityData response = legalEntityService.createLegalEntity(createLegalEntity);
    return new ResponseEntity<>(
        ServerResponse.success(
            "Successfully created new Legal entity profile", response, requestPath),
        HttpStatus.CREATED);
  }
}
