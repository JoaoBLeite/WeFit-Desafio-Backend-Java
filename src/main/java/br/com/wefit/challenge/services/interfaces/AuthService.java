package br.com.wefit.challenge.services.interfaces;

import br.com.wefit.challenge.exceptions.auth.InvalidJwtAuthenticationException;
import br.com.wefit.challenge.model.vo.request.auth.AccountCredentials;
import br.com.wefit.challenge.model.vo.response.auth.TokenData;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

public interface AuthService {

  /**
   * Authenticates a user and generates JWT tokens upon successful authentication.
   *
   * @param accountCredentials contains username and password for authentication
   * @return TokenData containing new access and refresh tokens
   * @throws BadCredentialsException if invalid credentials are provided
   * @throws DisabledException if the user account is disabled or locked
   */
  TokenData signing(AccountCredentials accountCredentials);

  /**
   * Refreshes the authentication tokens using a valid refresh token.
   *
   * @param refreshToken the valid refresh token to use for generating new tokens
   * @return TokenData containing new access and refresh tokens
   * @throws InvalidJwtAuthenticationException if the refresh token is invalid or expired
   */
  TokenData refresh(String refreshToken);
}
