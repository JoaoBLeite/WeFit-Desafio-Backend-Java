package br.com.wefit.challenge.services;

import java.util.List;

import br.com.wefit.challenge.enums.AccountStatus;
import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.model.vo.request.auth.AccountCredentials;
import br.com.wefit.challenge.model.vo.response.auth.TokenData;
import br.com.wefit.challenge.security.jwt.JwtTokenProvider;
import br.com.wefit.challenge.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @Override
  public TokenData signing(AccountCredentials accountCredentials) {
    log.info("Attempting to signing user with username: {}", accountCredentials.email());
    String username = accountCredentials.email();
    AccountStatus userStatus = null;

    try {
      User user = (User) userDetailsService.loadUserByUsername(username);
      userStatus = user.getStatus();

      log.debug("Authentication process: Authenticating credentials for user [{}]", username);
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, accountCredentials.password()));

      log.info("Authentication successful for user [{}]. Generating access token.", username);
      return tokenProvider.createAccessToken(username, List.of(user.getRole().toString()));
    } catch (BadCredentialsException | UsernameNotFoundException ex) {
      log.warn(
          "Authentication failed for user [{}]: Invalid username/password supplied. Error: {}",
          username,
          ex.getMessage());
      throw new BadCredentialsException("Invalid username/password supplied!");
    } catch (DisabledException | LockedException ex) {
      log.warn(
          "Authentication denied for user [{}]: Account is {}. Error: {}",
          username,
          userStatus,
          ex.getMessage());
      throw new DisabledException(String.format("The user is currently %s!", userStatus));
    } catch (Exception ex) {
      log.error(
          "An unexpected error occurred during sign-in for user [{}]. Error: {}",
          username,
          ex.getMessage(),
          ex);
      throw ex;
    }
  }

  @Override
  public TokenData refresh(String refreshToken) {
    log.info("Token refresh process: Attempting to refresh access token.");

    try {
      TokenData tokenData = tokenProvider.refreshToken(refreshToken);
      log.info("Token refresh process: Access token successfully refreshed.");
      return tokenData;
    } catch (Exception ex) {
      log.error(
          "Token refresh process: Failed to refresh access token. Error: {}", ex.getMessage(), ex);
      throw ex;
    }
  }
}
