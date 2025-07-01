package br.com.wefit.challenge.mappers;

import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import br.com.wefit.challenge.model.vo.response.address.AddressData;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public interface AddressMapper {

  /**
   * Converts a {@code CreateAddress} Value Object to an {@code Address} entity.
   *
   * @param vo The {@code CreateAddress} object containing the address data.
   * @return An {@code Address} entity populated with data from the VO, or {@code null} if the input
   *     VO is {@code null}.
   */
  Address toEntity(CreateAddress vo);

  /**
   * Converts an {@code Address} entity to an {@code AddressData} data transfer object.
   *
   * @param entity The {@code Address} entity to convert.
   * @return An {@code AddressData} object populated with data from the entity, or {@code null} if
   *     the input entity is {@code null}.
   */
  AddressData toData(Address entity);
}
