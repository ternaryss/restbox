package pl.tss.restbox.core.domain.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * Standardized page structure DTO for paginated requests.
 *
 * @author TSS
 */
@Data
@JsonDeserialize(builder = PageDto.PageDtoBuilder.class)
public class PageDto {

  private Integer page;
  private Integer pages;
  private Integer size;
  private List<? extends PageableDto> content;

  @Builder
  public PageDto(Integer page, Integer pages, Integer size, List<? extends PageableDto> content) {
    this.page = page;
    this.pages = pages;
    this.size = size;
    this.content = content;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class PageDtoBuilder {
  }

}
