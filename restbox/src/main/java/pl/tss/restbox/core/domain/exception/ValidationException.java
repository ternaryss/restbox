package pl.tss.restbox.core.domain.exception;

import java.util.List;

import pl.tss.restbox.core.domain.dto.ApiErrDetails;

/**
 * Exception used to signal validation API validation errors.
 *
 * @author TSS
 */
public class ValidationException extends ApiException {

  private static final long serialVersionUID = -961364560293020803L;

  public ValidationException(List<ApiErrDetails> details) {
    super(400, "err.validation", details);
  }

}
