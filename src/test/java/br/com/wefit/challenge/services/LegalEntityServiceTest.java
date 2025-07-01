package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockAddress;
import static br.com.wefit.challenge.services.fixtures.LegalEntityTestFixtures.getMockCreateLegalEntity;
import static br.com.wefit.challenge.services.fixtures.LegalEntityTestFixtures.getMockLegalEntityData;
import static br.com.wefit.challenge.services.fixtures.LegalEntityTestFixtures.getMockLegalEntityProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.wefit.challenge.mappers.LegalEntityMapper;
import br.com.wefit.challenge.model.entities.LegalEntityProfile;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import br.com.wefit.challenge.model.vo.request.legalentity.CreateLegalEntity;
import br.com.wefit.challenge.model.vo.response.legalentity.LegalEntityData;
import br.com.wefit.challenge.repositories.LegalEntityRepository;
import br.com.wefit.challenge.services.interfaces.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LegalEntityServiceTest {

  @Mock private AddressService addressServiceMock;
  @Mock private LegalEntityMapper legalEntityMapperMock;
  @Mock private LegalEntityRepository legalEntityRepositoryMock;

  @InjectMocks private LegalEntityServiceImpl legalEntityService;

  private final LegalEntityMapper legalEntityMapper = Mappers.getMapper(LegalEntityMapper.class);

  private LegalEntityProfile legalEntityProfile;
  private CreateLegalEntity createLegalEntity;

  @BeforeEach
  void setUp() {
    legalEntityProfile = getMockLegalEntityProfile();
    createLegalEntity =
        getMockCreateLegalEntity(legalEntityProfile, legalEntityProfile.getAddress());

    when(addressServiceMock.createAddress(createLegalEntity.address()))
        .thenReturn(getMockAddress());
  }

  @Test
  @DisplayName("Should successfully create a legal entity profile")
  void givenCreateLegalEntityData_whenCreateLegalEntity_thenCreateProfileSuccessfully() {
    // Arrange:
    when(legalEntityMapperMock.toEntity(createLegalEntity)).thenReturn(legalEntityProfile);
    when(legalEntityRepositoryMock.save(legalEntityProfile)).thenReturn(legalEntityProfile);
    when(legalEntityMapperMock.toData(legalEntityProfile))
        .thenReturn(getMockLegalEntityData(legalEntityProfile));

    // Act:
    LegalEntityData response = legalEntityService.createLegalEntity(createLegalEntity);

    // Assert:
    assertNotNull(response);
    assertEquals(legalEntityProfile.getId(), response.id());
    assertEquals(legalEntityProfile.getCnpj(), response.cnpj());
    assertEquals(legalEntityProfile.getResponsibleCpf(), response.responsibleCpf());
    assertEquals(legalEntityProfile.getName(), response.name());
    assertEquals(legalEntityProfile.getEmail(), response.email());
    assertEquals(legalEntityProfile.getCellPhone(), response.cellPhone());
    assertEquals(legalEntityProfile.getPhoneNumber(), response.phoneNumber());
    assertEquals(legalEntityProfile.getCreatedAt(), response.createdAt());
    assertEquals(legalEntityProfile.getLastUpdatedAt(), response.lastUpdatedAt());

    verify(addressServiceMock).createAddress(any(CreateAddress.class));
    verify(legalEntityMapperMock).toEntity(any(CreateLegalEntity.class));
    verify(legalEntityRepositoryMock).save(any(LegalEntityProfile.class));
    verify(legalEntityMapperMock).toData(any(LegalEntityProfile.class));
  }

  @Test
  @DisplayName("Should throw exception when legal entity creation fails at repository layer")
  void givenRepositoryIssue_whenCreateLegalEntity_throwException() {
    // Arrange:
    when(legalEntityMapperMock.toEntity(createLegalEntity)).thenReturn(legalEntityProfile);
    String exceptionMessage = "Repository error";
    when(legalEntityRepositoryMock.save(legalEntityProfile))
        .thenThrow(new RuntimeException(exceptionMessage));

    // Act:
    RuntimeException exception =
        assertThrows(
            RuntimeException.class, () -> legalEntityService.createLegalEntity(createLegalEntity));

    // Assert:
    assertNotNull(exception);
    assertEquals(exceptionMessage, exception.getMessage());

    verify(addressServiceMock).createAddress(any(CreateAddress.class));
    verify(legalEntityMapperMock).toEntity(any(CreateLegalEntity.class));
    verify(legalEntityRepositoryMock).save(any(LegalEntityProfile.class));
    verify(legalEntityMapperMock, never()).toData(any(LegalEntityProfile.class));
  }

  @Test
  @DisplayName("Should throw exception when legal entity creation fails at mapper layer")
  void givenMapperIssue_whenCreateLegalEntity_throwException() {
    // Arrange:
    String exceptionMessage = "Mapper error";
    when(legalEntityMapperMock.toEntity(createLegalEntity))
        .thenThrow(new RuntimeException(exceptionMessage));

    // Act:
    RuntimeException exception =
        assertThrows(
            RuntimeException.class, () -> legalEntityService.createLegalEntity(createLegalEntity));

    // Assert:
    assertNotNull(exception);
    assertEquals(exceptionMessage, exception.getMessage());

    verify(addressServiceMock).createAddress(any(CreateAddress.class));
    verify(legalEntityMapperMock).toEntity(any(CreateLegalEntity.class));
    verify(legalEntityRepositoryMock, never()).save(any(LegalEntityProfile.class));
    verify(legalEntityMapperMock, never()).toData(any(LegalEntityProfile.class));
  }
}
