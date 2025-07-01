package br.com.wefit.challenge.model.vo.request.legalentity;

import static br.com.wefit.challenge.model.vo.request.ValidationPatterns.CNPJ_STRICT_FORMAT;
import static br.com.wefit.challenge.model.vo.request.ValidationPatterns.CPF_STRICT_FORMAT;

import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateLegalEntity(
    @NotBlank(message = "CNPJ is required")
        @Pattern(
            regexp = CNPJ_STRICT_FORMAT,
            message = "Invalid CNPJ format. Expected XX.XXX.XXX/XXXX-XX")
        String cnpj,
    @NotBlank(message = "Responsible CPF is required")
        @Pattern(
            regexp = CPF_STRICT_FORMAT,
            message = "Invalid CPF format. Expected XXX.XXX.XXX-XX")
        String responsibleCpf,
    @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot be longer than 100 characters")
        String name,
    @NotBlank(message = "Email is required") @Email(message = "Email out of pattern") String email,
    @NotBlank(message = "Cell phone is required")
        @Size(max = 100, message = "Cell phone cannot be longer than 100 characters")
        String cellPhone,
    @NotBlank(message = "Phone number is required")
        @Size(max = 100, message = "Phone number cannot be longer than 100 characters")
        String phoneNumber,
    @NotNull(message = "Address is required") @Valid CreateAddress address) {}
