package br.com.wefit.challenge.mappers;

import br.com.wefit.challenge.model.entities.LegalEntityProfile;
import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring",
    uses = {AddressMapper.class})
public interface LegalEntityMapper {

  /**
   * Converts a {@code CreateLegalEntity} Value Object to a {@code LegalEntityProfile} entity.
   *
   * <p>Note: The 'address' field is explicitly ignored during this mapping and needs to be handled
   * separately.
   *
   * @param vo The {@code CreateLegalEntity} object containing the legal entity data.
   * @return A {@code LegalEntityProfile} entity populated with data from the VO, or {@code null} if
   *     the input VO is {@code null}.
   */
  @Mapping(target = "address", ignore = true)
  LegalEntityProfile toEntity(CreateLegalEntity vo);

  /**
   * Converts a {@code LegalEntityProfile} entity to a {@code LegalEntityData} data transfer object.
   *
   * @param entity The {@code LegalEntityProfile} entity to convert.
   * @return A {@code LegalEntityData} object populated with data from the entity, or {@code null}
   *     if the input entity is {@code null}.
   */
  LegalEntityData toData(LegalEntityProfile entity);
}
