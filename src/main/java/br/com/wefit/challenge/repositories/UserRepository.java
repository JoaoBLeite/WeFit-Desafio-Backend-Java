package br.com.wefit.challenge.repositories;

import br.com.wefit.challenge.model.entities.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Finds a user by their email address.
   *
   * @param email The email address of the user to find.
   * @return An {@link Optional} containing the found {@link User} if exists, or an empty {@link
   *     Optional} otherwise.
   */
  Optional<User> findByEmail(String email);
}
