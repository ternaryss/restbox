package pl.tss.restbox.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.tss.restbox.core.domain.filter.Sortable;

/**
 * External short movie info.
 *
 * @author TSS
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = MovieDto.MovieDtoBuilder.class)
public class MovieDto extends PageableDto {

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

  /**
   * Definition of sorting for movie DTO.
   */
  public enum SortColumn implements Sortable {

    MOV_ID("movid", "movie.movId"), TITLE("title", "movie.title"), GENERE("genere", "movie.genere.name"),
    PREMIERE("premiere", "movie.premiere"), RATE("rate", "movie.rate"), LENGTH("length", "movie.length"),
    COUNTRY("country", "movie.country.name"), ACT("act", "movie.act");

    private final String field;
    private final String query;

    SortColumn(String field, String query) {
      this.field = field;
      this.query = query;
    }

    @Override
    public Sortable[] getColumns() {
      return SortColumn.values();
    }

    @Override
    public String getField() {
      return field;
    }

    @Override
    public String getQuery() {
      return query;
    }

  }

}
