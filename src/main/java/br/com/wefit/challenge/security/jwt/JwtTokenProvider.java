package br.com.wefit.challenge.security.jwt;

import br.com.wefit.challenge.exceptions.auth.InvalidJwtAuthenticationException;
import br.com.wefit.challenge.model.vo.response.auth.TokenData;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.expire-length}")
  private long validityInMilliseconds;

  private final UserDetailsService userDetailsService;

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String ROLES_LABEL = "roles";

  @Autowired
  public JwtTokenProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  Algorithm algorithm = null;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    algorithm = Algorithm.HMAC256(secretKey.getBytes());
  }

  /**
   * Creates a new access token and refresh token for the given username and roles.
   *
   * @param username the username to include in the tokens
   * @param roles the roles/authorities to include in the tokens
   * @return TokenData containing both access and refresh tokens
   */
  public TokenData createAccessToken(String username, List<String> roles) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    var accessToken = getAccessToken(username, roles, now, validity);
    var refreshToken = getRefreshToken(username, roles, now);

    return TokenData.builder()
        .username(username)
        .authenticated(true)
        .created(now.getTime() / 1000)
        .expiration(validity.getTime() / 1000)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  /**
   * Refreshes an access token using a valid refresh token.
   *
   * @param refreshToken the refresh token to use
   * @return new TokenData with fresh access and refresh tokens
   */
  public TokenData refreshToken(String refreshToken) {
    if (refreshToken.contains(BEARER_PREFIX)) {
      refreshToken = refreshToken.substring(BEARER_PREFIX.length());
    }

    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(refreshToken);

    String username = decodedJWT.getSubject();
    List<String> roles = decodedJWT.getClaim(ROLES_LABEL).asList(String.class);

    return createAccessToken(username, roles);
  }

  /**
   * Generates a JWT access token with specified claims.
   *
   * @param username the subject of the token
   * @param roles the roles to include
   * @param now token issue time
   * @param validity token expiration time
   * @return signed JWT access token
   */
  private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
    String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

    return JWT.create()
        .withClaim(ROLES_LABEL, roles)
        .withIssuedAt(now)
        .withExpiresAt(validity)
        .withSubject(username)
        .withIssuer(issuerUrl)
        .sign(algorithm)
        .strip();
  }

  /**
   * Generates a JWT refresh token with longer validity than access token.
   *
   * @param username the subject of the token
   * @param roles the roles to include
   * @param now token issue time
   * @return signed JWT refresh token
   */
  private String getRefreshToken(String username, List<String> roles, Date now) {
    Date validityRefreshToken = new Date(now.getTime() + (3 * validityInMilliseconds));
    return JWT.create()
        .withClaim(ROLES_LABEL, roles)
        .withIssuedAt(now)
        .withExpiresAt(validityRefreshToken)
        .withSubject(username)
        .sign(algorithm)
        .strip();
  }

  /**
   * Creates Authentication object from JWT token.
   *
   * @param token the JWT token to authenticate
   * @return Authentication object with user details
   */
  public Authentication getAuthentication(String token) {
    DecodedJWT decodedJWT = decodedToken(token);
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /**
   * Decodes and verifies a JWT token.
   *
   * @param token the JWT token to decode
   * @return decoded JWT claims
   * @throws JWTVerificationException if token is invalid
   */
  private DecodedJWT decodedToken(String token) {
    Algorithm decodeAlgorithm = Algorithm.HMAC256(secretKey.getBytes());
    JWTVerifier verifier = JWT.require(decodeAlgorithm).build();
    return verifier.verify(token);
  }

  /**
   * Extracts JWT token from Authorization header.
   *
   * @param request the HTTP request
   * @return token without bearer prefix, or null if not present
   */
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (Objects.nonNull(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  /**
   * Validates JWT token expiration.
   *
   * @param token the JWT token to validate
   * @return true if token is valid and not expired
   * @throws InvalidJwtAuthenticationException if token is invalid/expired
   */
  public boolean validateToken(String token) {
    try {
      DecodedJWT decodedJWT = decodedToken(token);
      return !decodedJWT.getExpiresAt().before(new Date());
    } catch (Exception e) {
      throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
    }
  }
}
