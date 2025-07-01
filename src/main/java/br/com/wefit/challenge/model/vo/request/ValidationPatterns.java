package br.com.wefit.challenge.model.vo.request;

/**
 * A utility class providing common regular expression patterns for data validation.
 *
 * <p>These patterns are designed to be used with Jakarta Bean Validation's {@link
 * jakarta.validation.constraints.Pattern} annotation for declarative validation on DTOs and
 * entities.
 */
public class ValidationPatterns {

  private ValidationPatterns() {}

  /**
   * Regex pattern for a strictly formatted Brazilian CNPJ (Cadastro Nacional da Pessoa Jurídica).
   *
   * <p>Format: {@code XX.XXX.XXX/XXXX-XX}
   */
  public static final String CNPJ_STRICT_FORMAT = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}-\\d{2}$";

  /**
   * Regex pattern for a strictly formatted Brazilian CPF (Cadastro de Pessoa Física).
   *
   * <p>Format: {@code XXX.XXX.XXX-XX}
   */
  public static final String CPF_STRICT_FORMAT = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
}
