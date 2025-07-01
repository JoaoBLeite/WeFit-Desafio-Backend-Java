package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.services.fixtures.UserTestFixtures.getMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

  @Mock private UserRepository userRepositoryMock;

  @InjectMocks private UserDetailsServiceImpl userDetailsService;

  private User user;
  private String username;

  @BeforeEach
  void setUp() {
    user = getMockUser();
    username = user.getUsername();
  }

  @Test
  @DisplayName("Should successfully load user data")
  void givenUsername_whenLoadByUsername_thenReturnUserDetails() {
    // Arrange:
    when(userRepositoryMock.findByEmail(username)).thenReturn(Optional.of(user));

    // Act:
    User response = (User) userDetailsService.loadUserByUsername(username);

    // Assert:
    assertNotNull(response);
    verify(userRepositoryMock).findByEmail(username);
  }

  @Test
  @DisplayName("Should throw exception when no user found")
  void givenNonExistingUsername_whenLoadByUsername_throwUsernameNotFoundException() {
    // Arrange:
    when(userRepositoryMock.findByEmail(username)).thenReturn(Optional.empty());

    // Act:
    UsernameNotFoundException exception =
        assertThrows(
            UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

    // Assert:
    assertNotNull(exception);
    assertEquals("Username " + username + " not found!", exception.getMessage());
    verify(userRepositoryMock).findByEmail(username);
  }
}
