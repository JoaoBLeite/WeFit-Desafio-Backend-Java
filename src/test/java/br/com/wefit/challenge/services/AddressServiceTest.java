package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockAddress;
import static br.com.wefit.challenge.services.fixtures.AddressTestFixtures.getMockCrateAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.wefit.challenge.mappers.AddressMapper;
import br.com.wefit.challenge.model.entities.Address;
import br.com.wefit.challenge.model.vo.request.address.CreateAddress;
import br.com.wefit.challenge.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

  @Mock private AddressMapper addressMapperMock;
  @Mock private AddressRepository addressRepositoryMock;

  @InjectMocks private AddressServiceImpl addressService;

  private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

  private Address address;
  private CreateAddress createAddress;

  @BeforeEach
  void setUp() {
    address = getMockAddress();
    createAddress = getMockCrateAddress(address);
  }

  @Test
  @DisplayName("Should successfully create an address")
  void givenCreateAddressVO_whenCreateAddress_theCreateAddressSuccessfully() {
    // Arrange:
    when(addressMapperMock.toEntity(createAddress))
        .thenAnswer(arg -> addressMapper.toEntity(arg.getArgument(0)));
    when(addressRepositoryMock.save(any(Address.class))).thenReturn(address);

    // Act:
    Address response = addressService.createAddress(createAddress);

    // Assert:
    assertNotNull(response);
    verify(addressMapperMock).toEntity(createAddress);
    verify(addressRepositoryMock).save(any(Address.class));
  }

  @Test
  @DisplayName("Should throw exception when address creation fails at mapper layer")
  void givenMapperIssue_whenCreateAddress_thenThrowException() {
    // Arrange:
    String exceptionMessage = "Mapper error";
    when(addressMapperMock.toEntity(createAddress))
        .thenThrow(new RuntimeException(exceptionMessage));

    // Act:
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> addressService.createAddress(createAddress));

    // Assert:
    assertEquals(exceptionMessage, exception.getMessage());
    verify(addressMapperMock).toEntity(createAddress);
    verify(addressRepositoryMock, never()).save(any(Address.class));
  }

  @Test
  @DisplayName("Should throw exception when address creation fails at repository layer")
  void givenRepositoryIssue_whenCreateAddress_thenThrowException() {
    // Arrange:
    when(addressMapperMock.toEntity(createAddress))
        .thenAnswer(arg -> addressMapper.toEntity(arg.getArgument(0)));
    String exceptionMessage = "Database error";
    when(addressRepositoryMock.save(any(Address.class)))
        .thenThrow(new RuntimeException(exceptionMessage));

    // Act:
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> addressService.createAddress(createAddress));

    // Assert:
    assertEquals(exceptionMessage, exception.getMessage());
    verify(addressMapperMock).toEntity(createAddress);
    verify(addressRepositoryMock).save(any(Address.class));
  }
}
