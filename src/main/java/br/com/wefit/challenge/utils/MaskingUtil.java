package br.com.wefit.challenge.utils;

import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class MaskingUtil {

  // Private constructor to prevent instantiation
  private MaskingUtil() {}

  private static final int UUID_MASK_LENGTH = 8;
  private static final int CNPJ_MASK_LENGTH = 5;

  public static final String NOT_APPLICABLE = "N/A";
  public static final String MASK_SUFFIX = "...";

  /**
   * Masks a given {@link UUID} by returning its first configurable number of characters followed by
   * "...".
   *
   * @param uuid The {@link UUID} to be masked.
   * @return A string representing the masked ID (e.g., "12345678..."), or "N/A" if the provided
   *     {@code uuid} is {@code null}.
   */
  public static String mask(UUID uuid) {
    if (Objects.isNull(uuid)) {
      return NOT_APPLICABLE;
    }

    String uuidString = uuid.toString();
    int effectiveMaskLength = Math.min(UUID_MASK_LENGTH, uuidString.length());
    return uuidString.substring(0, effectiveMaskLength) + MASK_SUFFIX;
  }

  /**
   * Masks a given CNPJ by returning its first numbers of characters followed by "...".
   *
   * @param cnpj The {@link String} to be masked.
   * @return A string representing the masked CNPJ
   */
  public static String mask(String cnpj) {
    if (Objects.isNull(cnpj) || cnpj.trim().isEmpty()) {
      return NOT_APPLICABLE;
    }
    String cleanCnpj = cnpj.replaceAll("[^0-9]", "");

    int effectiveMaskLength = Math.min(CNPJ_MASK_LENGTH, cleanCnpj.length());
    return cleanCnpj.substring(0, effectiveMaskLength) + MASK_SUFFIX;
  }
}
