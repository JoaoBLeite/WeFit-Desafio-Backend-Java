package br.com.wefit.challenge.exceptions;

import br.com.wefit.challenge.exceptions.auth.InvalidJwtAuthenticationException;
import br.com.wefit.challenge.model.vo.response.ServerResponse;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

  /*
   * Handling exceptions from family 4xx
   */

  @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ServerResponse<?>> handleBadRequestExceptions(
      Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ServerResponse.error(ex.getMessage(), "BAD REQUEST", request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ServerResponse<?>> handleBadRequestArgNotValidExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    List<String> errorList =
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
    return new ResponseEntity<>(
        ServerResponse.error(errorList, "BAD REQUEST", request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    BadCredentialsException.class,
    DisabledException.class,
    AccountExpiredException.class
  })
  public ResponseEntity<ServerResponse<?>> handleUnauthorizedExceptions(
      Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ServerResponse.error(ex.getMessage(), "UNAUTHORIZED", request.getDescription(false)),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({
    InvalidJwtAuthenticationException.class,
    SecurityException.class,
    AccessDeniedException.class,
  })
  public ResponseEntity<ServerResponse<?>> handleForbiddenExceptions(
      Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ServerResponse.error(ex.getMessage(), "FORBIDDEN", request.getDescription(false)),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({UsernameNotFoundException.class})
  public ResponseEntity<ServerResponse<?>> handleNotFoundExceptions(
      Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ServerResponse.error(ex.getMessage(), "NOT FOUND", request.getDescription(false)),
        HttpStatus.NOT_FOUND);
  }

  /*
   * Handling exceptions from family 5xx
   */

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ServerResponse<?>> handlerUnexpectedExceptions(
      Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ServerResponse.error(
            "Unexpected internal server error!",
            "INTERNAL SERVER ERROR",
            request.getDescription(false)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
