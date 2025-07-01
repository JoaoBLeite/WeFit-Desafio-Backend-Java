package br.com.wefit.challenge.model.vo.response.legalentity;

import br.com.wefit.challenge.model.vo.response.address.AddressData;
import java.util.UUID;

public record LegalEntityData(
    UUID id,
    String cnpj,
    String responsibleCpf,
    String name,
    String email,
    String cellPhone,
    String phoneNumber,
    AddressData address,
    Long createdAt,
    Long lastUpdatedAt) {}
