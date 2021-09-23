package pl.tss.restbox.rest.infra;

import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.dto.ApiError;
import pl.tss.restbox.core.domain.exception.ApiException;

/**
 * Global REST API error handler.
 *
 * @author TSS
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class RestApiErrHandler extends ResponseEntityExceptionHandler {

  private final ResourceBundle msgBundle;

  public RestApiErrHandler(Optional<ResourceBundle> msgBundle) {
    this.msgBundle = msgBundle.orElse(null);
  }

  @ExceptionHandler(ApiException.class)
  protected ResponseEntity<ApiError> handleApiException(ApiException ex) {
    log.debug("API error occurred", ex);
    ex.getDetails()
        .forEach(det -> det.setMessage(msgBundle != null ? msgBundle.getString(det.getMessage()) : det.getMessage()));
    ApiError error = ApiError.builder()
        .message(msgBundle != null ? msgBundle.getString(ex.getMessage()) : ex.getMessage()).details(ex.getDetails())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handleException(Exception ex) {
    log.debug("Internal server error occurred", ex);
    String errCode = "err.internal";
    ApiError error = ApiError.builder().message(msgBundle != null ? msgBundle.getString(errCode) : errCode).build();

    return ResponseEntity.status(500).body(error);
  }

}
