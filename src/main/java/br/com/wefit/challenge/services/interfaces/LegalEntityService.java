package br.com.wefit.challenge.services.interfaces;

import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;

public interface LegalEntityService {

  /**
   * Creates a new legal entity profile in the system.
   *
   * @param vo The {@link CreateLegalEntity} value object containing the data for the new legal
   *     entity.
   * @return A {@link LegalEntityData} object representing the newly created legal entity.
   */
  LegalEntityData createLegalEntity(CreateLegalEntity vo);
}
