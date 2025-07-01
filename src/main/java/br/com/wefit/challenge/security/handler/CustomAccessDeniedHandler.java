package br.com.wefit.challenge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

    String details = request.getRequestURI();
    Long timestamp = Instant.now().getEpochSecond();

    response.setStatus(HttpStatus.FORBIDDEN.value());
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
                        "FORBIDDEN",
                        "path",
                        details,
                        "timestamp",
                        timestamp)));
  }
}
