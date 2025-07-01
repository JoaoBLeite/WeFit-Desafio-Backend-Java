package br.com.wefit.challenge.model.vo.response;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ServerResponse<P> {
  private final boolean success;
  private final Object message;
  private final String errorCode;
  private final P payload;
  private final String path;
  private final long timestamp;

  public ServerResponse(boolean success, Object message, String errorCode, P payload, String path) {
    this.success = success;
    this.message = message;
    this.errorCode = errorCode;
    this.payload = payload;
    this.path = removeUriPrefix(path);
    this.timestamp = Instant.now().getEpochSecond();
  }

  private String removeUriPrefix(String uri) {
    if (Objects.nonNull(uri) && uri.startsWith("uri=")) {
      return uri.substring(4);
    }
    return uri;
  }

  public static <P> ServerResponse<P> success(String message, P payload, String path) {
    return new ServerResponse<>(true, message, null, payload, path);
  }

  public static <P> ServerResponse<P> error(Object message, String errorCode, String path) {
    return new ServerResponse<>(false, message, errorCode, null, path);
  }
}
