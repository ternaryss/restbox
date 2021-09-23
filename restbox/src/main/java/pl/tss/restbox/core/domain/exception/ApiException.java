package pl.tss.restbox.core.domain.exception;

import java.util.List;

import lombok.Getter;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;

/**
 * Exception used to signal API errors.
 *
 * @author TSS
 */
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = -1310144002933647791L;

  @Getter
  private final int status;

  @Getter
  private final String message;

  @Getter
  private final List<ApiErrDetails> details;

  public ApiException(int status, String message, List<ApiErrDetails> details) {
    this.status = status;
    this.message = message;
    this.details = details;
  }

}
