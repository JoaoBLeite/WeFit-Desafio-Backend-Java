package br.com.wefit.challenge.services;

import static br.com.wefit.challenge.services.fixtures.AuthServiceTestFixtures.getMockAccountCredentials;
import static br.com.wefit.challenge.services.fixtures.UserTestFixtures.getMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.wefit.challenge.enums.AccountStatus;
import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.model.vo.request.auth.AccountCredentials;
import br.com.wefit.challenge.model.vo.response.auth.TokenData;
import br.com.wefit.challenge.security.jwt.JwtTokenProvider;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock private UserDetailsService userDetailsServiceMock;
  @Mock private AuthenticationManager authenticationManagerMock;
  @Mock private JwtTokenProvider jwtTokenProviderMock;

  @InjectMocks private AuthServiceImpl authService;

  private User userAccount;
  private AccountCredentials accountCredentials;

  @BeforeEach
  void setUp() {
    userAccount = getMockUser();
    accountCredentials = getMockAccountCredentials(userAccount);
  }

  @Test
  @DisplayName("Should successfully signing user")
  void givenAccountCredentials_whenSigning_thenReturnTokenData() {
    // Arrange:
    TokenData tokenData = new TokenData();
    when(userDetailsServiceMock.loadUserByUsername(accountCredentials.email()))
        .thenReturn(userAccount);
    when(jwtTokenProviderMock.createAccessToken(
            userAccount.getEmail(), List.of(userAccount.getRole().toString())))
        .thenReturn(tokenData);

    // Act:
    TokenData response = authService.signing(accountCredentials);

    // Assert:
    assertNotNull(response);
    verify(authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  @DisplayName("Should throw BadCredentialsException when username does not exist")
  void givenNonExistingUsername_whenSigning_throwBadCredentialsException() {
    // Arrange:
    String exceptionMessage = "Username not found!";
    when(userDetailsServiceMock.loadUserByUsername(accountCredentials.email()))
        .thenThrow(new UsernameNotFoundException(exceptionMessage));

    // Act:
    BadCredentialsException exception =
        assertThrows(BadCredentialsException.class, () -> authService.signing(accountCredentials));

    // Assert:
    assertNotNull(exception);
    assertEquals("Invalid username/password supplied!", exception.getMessage());
    verify(authenticationManagerMock, never())
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtTokenProviderMock, never()).createAccessToken(anyString(), anyList());
  }

  @Test
  @DisplayName("Should throw BadCredentialsException when password is invalid")
  void givenInvalidPassword_whenSigning_throwBadCredentialsException() {
    // Arrange:
    String exceptionMessage = "Authentication exception!";
    when(userDetailsServiceMock.loadUserByUsername(accountCredentials.email()))
        .thenReturn(userAccount);
    when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException(exceptionMessage));

    // Act:
    BadCredentialsException exception =
        assertThrows(BadCredentialsException.class, () -> authService.signing(accountCredentials));

    // Assert:
    assertNotNull(exception);
    assertEquals("Invalid username/password supplied!", exception.getMessage());
    verify(jwtTokenProviderMock, never()).createAccessToken(anyString(), anyList());
  }

  @Test
  @DisplayName("Should throw DisabledException when user account is not enabled")
  void givenNonEnabledUserCredentials_whenSigning_throwDisabledException() {
    // Arrange:
    userAccount.setStatus(AccountStatus.LOCKED);
    String exceptionMessage = "Authentication exception!";
    when(userDetailsServiceMock.loadUserByUsername(accountCredentials.email()))
        .thenReturn(userAccount);
    when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new DisabledException(exceptionMessage));

    // Act:
    DisabledException exception =
        assertThrows(DisabledException.class, () -> authService.signing(accountCredentials));

    // Assert:
    assertNotNull(exception);
    assertEquals(
        String.format("The user is currently %s!", userAccount.getStatus()),
        exception.getMessage());
    verify(jwtTokenProviderMock, never()).createAccessToken(anyString(), anyList());
  }

  @Test
  @DisplayName("Should successfully refresh token and return TokenData")
  void givenRefreshToken_whenRefreshToken_thenReturnTokenData() {
    // Arrange
    String refreshToken = "RefreshToken";
    TokenData expectedToken = new TokenData();

    when(jwtTokenProviderMock.refreshToken(refreshToken)).thenReturn(expectedToken);

    // Act
    TokenData result = authService.refresh(refreshToken);

    // Assert
    assertEquals(expectedToken, result);
  }
}
