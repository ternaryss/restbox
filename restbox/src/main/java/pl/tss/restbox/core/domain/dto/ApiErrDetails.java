package pl.tss.restbox.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * Details for API error.
 *
 * @author TSS
 */
@Data
@JsonDeserialize(builder = ApiErrDetails.ApiErrDetailsBuilder.class)
public class ApiErrDetails {

  private String field;
  private String message;

  @Builder
  public ApiErrDetails(String field, String message) {
    this.field = field;
    this.message = message;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class ApiErrDetailsBuilder {
  }

}
