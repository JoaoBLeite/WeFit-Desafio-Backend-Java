package br.com.wefit.challenge.services.fixtures;

import br.com.wefit.challenge.enums.AccountStatus;
import br.com.wefit.challenge.enums.UserRole;
import br.com.wefit.challenge.model.entities.User;
import java.time.Instant;
import java.util.UUID;

public class UserTestFixtures {

  // Prevent instantiation
  private UserTestFixtures() {}

  public static User getMockUser() {
    return User.builder()
        .id(UUID.randomUUID())
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@domain.com")
        .password("encodedPass")
        .role(UserRole.REGULAR)
        .status(AccountStatus.ACTIVE)
        .createdAt(Instant.now().getEpochSecond())
        .lastUpdatedAt(Instant.now().getEpochSecond())
        .build();
  }
}
