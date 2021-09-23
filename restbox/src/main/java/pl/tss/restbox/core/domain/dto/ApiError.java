package pl.tss.restbox.core.domain.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * External API error.
 *
 * @author TSS
 */
@Data
@JsonDeserialize(builder = ApiError.ApiErrorBuilder.class)
public class ApiError {

  private String message;
  private String timestamp;
  private List<ApiErrDetails> details;

  @Builder
  public ApiError(String message, List<ApiErrDetails> details) {
    this.message = message;
    this.details = details;
    this.timestamp = OffsetDateTime.now().withNano(0).toString();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class ApiErrorBuilder {
  }

}
