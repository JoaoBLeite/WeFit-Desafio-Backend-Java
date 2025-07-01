package br.com.wefit.challenge.model.vo.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TokenData {
  private String username;
  private boolean authenticated;
  private long created;
  private long expiration;
  private String accessToken;
  private String refreshToken;
}
