package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.utils.MaskingUtil.mask;

import br.com.wefit.challenge.mappers.LegalEntityMapper;
import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.entities.LegalEntityProfile;
import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;
import br.com.wefit.challenge.repositories.LegalEntityRepository;
import br.com.wefit.challenge.services.interfaces.AddressService;
import br.com.wefit.challenge.services.interfaces.LegalEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalEntityServiceImpl implements LegalEntityService {

  private final AddressService addressService;

  private final LegalEntityMapper legalEntityMapper;
  private final LegalEntityRepository legalEntityRepository;

  @Override
  @Transactional
  public LegalEntityData createLegalEntity(CreateLegalEntity vo) {
    log.info("Attempting to create legal entity for CNPJ: {}", mask(vo.cnpj()));

    try {
      log.debug(
          "Attempting to create address for legal entity CNPJ: {}", mask(vo.cnpj()));
      Address address = addressService.createAddress(vo.address());

      LegalEntityProfile legalEntity = legalEntityMapper.toEntity(vo);
      legalEntity.setAddress(address);
      legalEntityRepository.save(legalEntity);

      log.info(
          "Successfully created legal entity with ID: {} for CNPJ: {}",
          mask(legalEntity.getId()),
          mask(legalEntity.getCnpj()));
      return legalEntityMapper.toData(legalEntity);
    } catch (Exception e) {
      log.error(
          "Failed to create legal entity for CNPJ: {}. Error: {}",
          mask(vo.cnpj()),
          e.getMessage(),
          e);
      throw e;
    }
  }
}
