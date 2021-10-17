package pl.tss.restbox.core.domain.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Detailed movie external info.
 *
 * @author TSS
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = MovieDetailsDto.MovieDetailsDtoBuilder.class)
public class MovieDetailsDto extends MovieDto {

  private PersonDto director;
  private List<PersonDto> actors;

  @Builder
  public MovieDetailsDto(Integer movId, String title, String genere, String description, String premiere, Integer rate,
      Integer length, String country, Boolean act, PersonDto director, List<PersonDto> actors) {
    super(movId, title, genere, description, premiere, rate, length, country, act);
    this.director = director;
    this.actors = actors;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class MovieDetailsDtoBuilder extends MovieDto.MovieDtoBuilder {

  }

}
