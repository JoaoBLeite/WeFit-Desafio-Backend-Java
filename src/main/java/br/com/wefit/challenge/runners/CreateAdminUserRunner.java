package br.com.wefit.challenge.runners;

import br.com.wefit.challenge.enums.AccountStatus;
import br.com.wefit.challenge.enums.UserRole;
import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.repositories.UserRepository;
import br.com.wefit.challenge.utils.PasswordUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * {@code ApplicationRunner} that creates a default administrator user account upon application
 * startup.
 *
 * <p>This runner is specifically designed for the "challenge" application context to provide an
 * initial user for demonstrating API usage without manual setup. It checks if an admin account with
 * the configured username already exists to prevent duplicate creation on subsequent application
 * restarts.
 *
 * <p>**Note:** The logging of the plain-text password is for demonstration/development purposes in
 * a challenge environment and should be removed or secured in a production application.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateAdminUserRunner implements ApplicationRunner {

  private final PasswordUtil passwordUtil;
  private final UserRepository userRepository;

  @Value("${security.constants.username}")
  private String adminUsername;

  @Value("${security.constants.password}")
  private String adminPassword;

  /**
   * Executes logic to create a default admin user account when the Spring Boot application starts.
   *
   * <p>This method is invoked after the application context has been fully initialized. It checks
   * for the existence of an admin user by email and creates one if it doesn't exist.
   *
   * @param args The application arguments.
   */
  @Override
  public void run(ApplicationArguments args) {
    Optional<User> optionalUser = userRepository.findByEmail(adminUsername);

    if (optionalUser.isPresent()) {
      log.debug("Admin account already exists. Skipping creation.");
      return;
    }

    try {
      User admin = createAdminUserAccount();
      userRepository.save(admin);

      log.debug("Admin account created successfully.");
      log.debug("username: {}", adminUsername);
      log.debug("password (NOT FOR PROD): {}", adminPassword);
    } catch (Exception ex) {
      log.error(
          "Failed to create admin account due to an unexpected error: {}", ex.getMessage(), ex);
    }
  }

  /**
   * Creates a default admin user account.
   *
   * @return A new {@link User} entity configured as an admin.
   */
  private User createAdminUserAccount() {
    return User.builder()
        .firstName("John")
        .lastName("Doe")
        .email(adminUsername)
        .password(passwordUtil.encode(adminPassword))
        .role(UserRole.ADMIN)
        .status(AccountStatus.ACTIVE)
        .build();
  }
}
