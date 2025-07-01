package br.com.wefit.challenge.services.fixtures;

import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.model.vo.request.auth.AccountCredentials;

public class AuthServiceTestFixtures {

  // Prevent instantiation
  private AuthServiceTestFixtures() {}

  public static AccountCredentials getMockAccountCredentials(User user) {
    return new AccountCredentials(user.getEmail(), "raw password");
  }
}
