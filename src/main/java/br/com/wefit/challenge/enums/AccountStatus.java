package br.com.wefit.challenge.enums;

/** Enum representing the status of a user account in the system. */
public enum AccountStatus {
  ACTIVE("ACTIVE"),
  DISABLED("DISABLED"),
  LOCKED("LOCKED");

  private final String status;

  AccountStatus(String status) {
    this.status = status;
  }

  public boolean isEnabled() {
    return this == ACTIVE;
  }

  public boolean isLocked() {
    return this == LOCKED;
  }

  @Override
  public String toString() {
    return status;
  }
}
