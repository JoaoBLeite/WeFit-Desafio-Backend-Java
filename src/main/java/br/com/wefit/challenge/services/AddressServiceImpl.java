package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.utils.MaskingUtil.mask;

import br.com.wefit.challenge.mappers.AddressMapper;
import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import br.com.wefit.challenge.repositories.AddressRepository;
import br.com.wefit.challenge.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressMapper addressMapper;
  private final AddressRepository addressRepository;

  @Override
  public Address createAddress(CreateAddress vo) {
    log.info("Attempting to create address for cep: {}", vo.cep());

    try {
      Address address = addressMapper.toEntity(vo);
      addressRepository.save(address);

      log.info(
          "Successfully created address with ID: {} for cep: {}",
          mask(address.getId()),
          address.getCep());
      return address;
    } catch (Exception e) {
      log.error("Failed to create address for email: {}. Error: {}", vo.cep(), e.getMessage(), e);
      throw e;
    }
  }
}
