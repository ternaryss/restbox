package pl.tss.restbox.core.domain.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for external movie data structure.
 *
 * @author TSS
 */
public class MovieDtoTest {

  @Test
  public void movieDtoSortColumnTest() {
    int expectedSize = 8;
    Assertions.assertEquals(expectedSize, MovieDto.SortColumn.values().length);

    String expectedMovId = "movid";
    String expectedMovIdQuery = "movie.movId";
    Assertions.assertEquals(expectedMovId, MovieDto.SortColumn.MOV_ID.getField());
    Assertions.assertEquals(expectedMovIdQuery, MovieDto.SortColumn.MOV_ID.getQuery());

    String expectedTitle = "title";
    String expectedTitleQuery = "movie.title";
    Assertions.assertEquals(expectedTitle, MovieDto.SortColumn.TITLE.getField());
    Assertions.assertEquals(expectedTitleQuery, MovieDto.SortColumn.TITLE.getQuery());

    String expectedGenere = "genere";
    String expectedGenereQuery = "movie.genere.name";
    Assertions.assertEquals(expectedGenere, MovieDto.SortColumn.GENERE.getField());
    Assertions.assertEquals(expectedGenereQuery, MovieDto.SortColumn.GENERE.getQuery());

    String expectedPremiere = "premiere";
    String expectedPremiereQuery = "movie.premiere";
    Assertions.assertEquals(expectedPremiere, MovieDto.SortColumn.PREMIERE.getField());
    Assertions.assertEquals(expectedPremiereQuery, MovieDto.SortColumn.PREMIERE.getQuery());

    String expectedRate = "rate";
    String expectedRateQuery = "movie.rate";
    Assertions.assertEquals(expectedRate, MovieDto.SortColumn.RATE.getField());
    Assertions.assertEquals(expectedRateQuery, MovieDto.SortColumn.RATE.getQuery());

    String expectedLength = "length";
    String expectedLengthQuery = "movie.length";
    Assertions.assertEquals(expectedLength, MovieDto.SortColumn.LENGTH.getField());
    Assertions.assertEquals(expectedLengthQuery, MovieDto.SortColumn.LENGTH.getQuery());

    String expectedCountry = "country";
    String expectedCountryQuery = "movie.country.name";
    Assertions.assertEquals(expectedCountry, MovieDto.SortColumn.COUNTRY.getField());
    Assertions.assertEquals(expectedCountryQuery, MovieDto.SortColumn.COUNTRY.getQuery());

    String expectedAct = "act";
    String expectedActQuery = "movie.act";
    Assertions.assertEquals(expectedAct, MovieDto.SortColumn.ACT.getField());
    Assertions.assertEquals(expectedActQuery, MovieDto.SortColumn.ACT.getQuery());
  }

}
