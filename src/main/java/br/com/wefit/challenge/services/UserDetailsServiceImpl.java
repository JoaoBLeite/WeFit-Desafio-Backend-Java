package br.com.wefit.challenge.services;

import br.com.wefit.challenge.model.entities.User;
import br.com.wefit.challenge.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Loads user details by the given identifier username.
   *
   * @param username the username identifier
   * @return UserDetails implementation containing the user's authentication information
   * @throws UsernameNotFoundException if no user is found with the given CPF
   * @see UserDetails
   * @see User
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Attempting to load user by email: {}", username);
    return userRepository
        .findByEmail(username)
        .orElseThrow(
            () -> {
              log.warn("User with email '{}' not found during authentication attempt.", username);
              return new UsernameNotFoundException("Username " + username + " not found!");
            });
  }
}
