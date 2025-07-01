package br.com.wefit.challenge.security.jwt;

import br.com.wefit.challenge.exceptions.auth.InvalidJwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider tokenProvider;

  @Autowired
  public JwtTokenFilter(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  /**
   * Custom filter implementation that processes JWT authentication.
   *
   * <p>Extracts the JWT token from the request, validates it, and if valid, sets the authentication
   * in the Spring Security context.
   *
   * @param servletRequest the incoming request
   * @param servletResponse the outgoing response
   * @param filterChain the filter chain to continue processing
   * @throws IOException if an I/O error occurs during filter processing
   * @throws ServletException if the request cannot be handled
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    try {
      String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

      if (Objects.nonNull(token) && tokenProvider.validateToken(token)) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        if (Objects.nonNull(authentication)) {
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

      filterChain.doFilter(servletRequest, servletResponse);
    } catch (InvalidJwtAuthenticationException ex) {
      handleInvalidJwtAuthenticationException(
          (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
    }
  }

  /**
   * Handles InvalidJwtAuthenticationException by sending a 401 Unauthorized HTTP response with a
   * JSON body containing error details.
   *
   * @param request the HttpServletRequest containing request details
   * @param response the HttpServletResponse to which the error response must be written
   * @throws IOException if an input or output exception occurs while wiring the response
   */
  private void handleInvalidJwtAuthenticationException(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    String details = request.getRequestURI();
    Long timestamp = Instant.now().getEpochSecond();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response
        .getWriter()
        .write(
            new ObjectMapper()
                .writeValueAsString(
                    Map.of(
                        "success",
                        false,
                        "message",
                        "You do not have permission to access this resource!",
                        "errorCode",
                        "UNAUTHORIZED",
                        "path",
                        details,
                        "timestamp",
                        timestamp)));
  }
}
