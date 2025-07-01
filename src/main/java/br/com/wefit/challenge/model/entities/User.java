package br.com.wefit.challenge.model.entities;

import br.com.wefit.challenge.enums.AccountStatus;
import br.com.wefit.challenge.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Types;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(Types.VARCHAR)
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "last_updated_at")
  private Long lastUpdatedAt;

  // --- Spring Security UserDetails Implementations ---

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(role);
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !status.isLocked();
  }

  @Override
  public boolean isEnabled() {
    return status.isEnabled();
  }

  // --- JPA Lifecycle Callbacks ---

  @PrePersist
  private void prePersist() {
    long now = Instant.now().getEpochSecond();
    this.createdAt = now;
    this.lastUpdatedAt = now;
  }

  @PreUpdate
  private void preUpdate() {
    this.lastUpdatedAt = Instant.now().getEpochSecond();
  }
}
