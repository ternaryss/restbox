package pl.tss.restbox.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * External short movie info.
 *
 * @author TSS
 */
@Data
@JsonDeserialize(builder = MovieDto.MovieDtoBuilder.class)
public class MovieDto {

  private Integer movId;
  private String title;
  private String genere;
  private String description;
  private String premiere;
  private Integer rate;
  private Integer length;
  private String country;
  private Boolean act;

  @Builder
  public MovieDto(Integer movId, String title, String genere, String description, String premiere, Integer rate,
      Integer length, String country, Boolean act) {
    this.movId = movId;
    this.title = title;
    this.genere = genere;
    this.description = description;
    this.premiere = premiere;
    this.rate = rate;
    this.length = length;
    this.country = country;
    this.act = act;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class MovieDtoBuilder {

  }

}
