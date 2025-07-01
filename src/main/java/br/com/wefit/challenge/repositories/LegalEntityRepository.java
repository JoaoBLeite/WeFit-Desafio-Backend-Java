package br.com.wefit.challenge.repositories;

import br.com.wefit.challenge.model.entities.LegalEntityProfile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalEntityRepository extends JpaRepository<LegalEntityProfile, UUID> {}
