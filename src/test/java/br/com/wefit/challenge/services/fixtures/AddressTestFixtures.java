package br.com.wefit.challenge.services.fixtures;

import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import br.com.wefit.challenge.model.vo.response.address.AddressData;
import java.util.UUID;

public class AddressTestFixtures {

  // Prevent instantiation
  private AddressTestFixtures() {}

  public static CreateAddress getMockCrateAddress(Address address) {
    return new CreateAddress(
        address.getCep(),
        address.getState(),
        address.getCity(),
        address.getNeighborhood(),
        address.getStreet(),
        address.getNumber(),
        address.getComplement());
  }

  public static AddressData getMockAddressData(Address address) {
    return new AddressData(
        address.getId(),
        address.getCep(),
        address.getState(),
        address.getCity(),
        address.getNeighborhood(),
        address.getStreet(),
        address.getNumber(),
        address.getComplement());
  }

  public static Address getMockAddress() {
    return Address.builder()
        .id(UUID.randomUUID())
        .cep("01452-001")
        .state("São Paulo")
        .city("São Paulo")
        .neighborhood("Jardim Paulistano")
        .street("Av. Brig. Faria Lima")
        .number("1853")
        .complement("Mezanino")
        .build();
  }
}
