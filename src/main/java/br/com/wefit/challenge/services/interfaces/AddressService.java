package br.com.wefit.challenge.services.interfaces;


import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;

public interface AddressService {

  /**
   * Creates a new address entity in the system.
   *
   * @param vo A {@link CreateAddress} object containing the details for the new address.
   * @return An {@link Address} entity representing the newly created and persisted address.
   */
  Address createAddress(CreateAddress vo);
}
