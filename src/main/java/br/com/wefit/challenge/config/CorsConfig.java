package br.com.wefit.challenge.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

public class CorsConfig implements CorsConfigurationSource {

  @Override
  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    CorsConfiguration configuration = new CorsConfiguration();
    // TODO: Add front-end url
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setAllowCredentials(false);
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setMaxAge(3600L);
    return configuration;
  }
}
