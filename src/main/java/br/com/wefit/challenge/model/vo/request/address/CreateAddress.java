package br.com.wefit.challenge.model.vo.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAddress(
    @NotBlank(message = "Address CEP is required")
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "Address CEP must be in format XXXXX-XXX")
        String cep,
    @NotBlank(message = "Address state is required")
        @Size(max = 50, message = "Address state cannot be longer than 50 characters")
        String state,
    @NotBlank(message = "Address city is required")
        @Size(max = 100, message = "Address city cannot be longer than 100 characters")
        String city,
    @NotBlank(message = "Address neighborhood is required")
        @Size(max = 100, message = "Address neighborhood cannot be longer than 100 characters")
        String neighborhood,
    @NotBlank(message = "Address street is required")
        @Size(max = 200, message = "Address street cannot be longer than 200 characters")
        String street,
    @NotBlank(message = "Address number is required")
        @Size(max = 20, message = "Address number cannot be longer than 20 characters")
        String number,
    @Size(max = 225, message = "Address complement cannot be longer than 225 characters")
        String complement) {}
