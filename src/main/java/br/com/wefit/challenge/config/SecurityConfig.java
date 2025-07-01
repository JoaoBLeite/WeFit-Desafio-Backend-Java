package br.com.wefit.challenge.config;

import br.com.wefit.challenge.enums.UserRole;
import br.com.wefit.challenge.security.handler.CustomAccessDeniedHandler;
import br.com.wefit.challenge.security.jwt.JwtTokenFilter;
import br.com.wefit.challenge.security.jwt.JwtTokenProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String ADMIN_ROLE = UserRole.ADMIN.name();
  private static final String MANAGER_ROLE = UserRole.MANAGER.name();

  @Value("${security.encoder.salt-length}")
  private int saltLength;

  @Value("${security.encoder.iterations}")
  private int iterations;

  @Bean
  public PasswordEncoder passwordEncoder() {
    Pbkdf2PasswordEncoder encoder =
        new Pbkdf2PasswordEncoder(
            "",
            saltLength,
            iterations,
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", encoder);

    DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
    passwordEncoder.setDefaultPasswordEncoderForMatches(encoder);

    return passwordEncoder;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtTokenProvider tokenProvider,
      CustomAccessDeniedHandler accessDeniedHandler)
      throws Exception {

    JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
    CorsConfig corsConfig = new CorsConfig();

    return http.httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorizeHttpRequest ->
                authorizeHttpRequest
                    // Public controllers:
                    .requestMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/h2-console/**",
                        "/webjars/**")
                    .permitAll()
                    // Admin-only POST endpoints:
                    .requestMatchers(HttpMethod.POST, "/user/v1")
                    .hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                    // Deny all
                    .requestMatchers("/users")
                    .denyAll()
                    // All other requests require authentication
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
        .cors(cors -> cors.configurationSource(corsConfig))
        .build();
  }
}
