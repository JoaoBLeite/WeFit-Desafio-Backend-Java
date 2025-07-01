package br.com.wefit.challenge.services.fixtures;

import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockAddress;
import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockAddressData;
import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockCrateAddress;

import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.entities.LegalEntityProfile;
import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;
import java.util.UUID;

public class LegalEntityTestFixtures {

  // Prevent instantiation
  private LegalEntityTestFixtures() {}

  public static CreateLegalEntity getMockCreateLegalEntity(
      LegalEntityProfile profile, Address address) {
    return new CreateLegalEntity(
        profile.getCnpj(),
        profile.getResponsibleCpf(),
        profile.getName(),
        profile.getEmail(),
        profile.getCellPhone(),
        profile.getPhoneNumber(),
        getMockCrateAddress(address));
  }

  public static LegalEntityData getMockLegalEntityData(LegalEntityProfile profile) {
    return new LegalEntityData(
        profile.getId(),
        profile.getCnpj(),
        profile.getResponsibleCpf(),
        profile.getName(),
        profile.getEmail(),
        profile.getCellPhone(),
        profile.getPhoneNumber(),
        getMockAddressData(profile.getAddress()),
        profile.getCreatedAt(),
        profile.getLastUpdatedAt());
  }

  public static LegalEntityProfile getMockLegalEntityProfile() {
    return LegalEntityProfile.builder()
        .id(UUID.randomUUID())
        .cnpj("12.345.678/0001-90")
        .responsibleCpf("987.654.321-00")
        .name("WeFit Digital Service Design")
        .email("contact@wefit.com.br")
        .cellPhone("11987654321")
        .phoneNumber("1130789876")
        .address(getMockAddress())
        .build();
  }
}
