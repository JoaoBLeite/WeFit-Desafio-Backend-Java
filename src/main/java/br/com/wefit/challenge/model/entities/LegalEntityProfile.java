package br.com.wefit.challenge.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "legal_entity_profiles")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalEntityProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(Types.VARCHAR)
  private UUID id;

  private String cnpj;

  @Column(name = "responsible_cpf")
  private String responsibleCpf;

  private String name;

  private String email;

  @Column(name = "cell_phone")
  private String cellPhone;

  @Column(name = "phone_number")
  private String phoneNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "last_updated_at")
  private Long lastUpdatedAt;

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
