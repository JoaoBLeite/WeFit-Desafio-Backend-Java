package br.com.wefit.challenge.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enumeration representing user roles in the app, implementing Spring Security's {@link
 * GrantedAuthority}.
 *
 * <p>These roles are used for authorization and access control throughout the application.
 *
 * @see GrantedAuthority
 */
public enum UserRole implements GrantedAuthority {
  ADMIN("ADMIN"),
  MANAGER("MANAGER"),
  REGULAR("REGULAR");

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  @Override
  public String toString() {
    return authority;
  }

  @Override
  public String getAuthority() {
    return "ROLE_" + authority;
  }
}
