package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.movie.GetMoviesCmd;
import pl.tss.restbox.core.domain.dto.MovieDto;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.filter.MoviesFilter;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.CountryRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Unit tests for invalid movies listing.
 *
 * @author TSS
 */
public class BadGetMoviesTest {

  private final Country[] mockedCountries = new Country[] { new Country("USA"), new Country("France"),
      new Country("New Zealand") };

  private final Genere[] mockedGeneries = new Genere[] { new Genere("Drama"), new Genere("Court drama"),
      new Genere("Comedy"), new Genere("Fiction") };

  private final Movie[] mockedMovies = new Movie[] {
      new Movie("The Shawshank Redemption", OffsetDateTime.parse("1994-09-10T00:00:00+02:00"), 9, 144, null,
          mockedCountries[0], mockedGeneries[0]),
      new Movie("Intouchables", OffsetDateTime.parse("2011-09-23T00:00:00+02:00"), 9, 112, null, mockedCountries[1],
          mockedGeneries[0]),
      new Movie("The Green Mile", OffsetDateTime.parse("1999-12-06T00:00:00+02:00"), 9, 188, null, mockedCountries[0],
          mockedGeneries[0]),
      new Movie("The Goodfather", OffsetDateTime.parse("1972-03-15T00:00:00+02:00"), 9, 175, null, mockedCountries[0],
          mockedGeneries[0]),
      new Movie("12 Angry Men", OffsetDateTime.parse("1957-04-10T00:00:00+02:00"), 9, 96, null, mockedCountries[0],
          mockedGeneries[1]) };

  private final MovieDto[] mockedMoviesDto = {
      MovieDto.builder().movId(1).title("The Shawshank Redemption").genere("Drama")
          .premiere(OffsetDateTime.parse("1994-09-10T00:00:00+02:00").withNano(0).toString()).rate(9).length(144)
          .country("USA").act(false).description("Test description").build(),
      MovieDto.builder().movId(2).title("Intouchables").genere("Drama")
          .premiere(OffsetDateTime.parse("2011-09-23T00:00:00+02:00").withNano(0).toString()).rate(9).length(112)
          .country("France").act(true).build(),
      MovieDto.builder().movId(3).title("The Green Mile").genere("Drama")
          .premiere(OffsetDateTime.parse("1999-12-06T00:00:00+02:00").withNano(0).toString()).rate(9).length(188)
          .country("USA").act(true).build(),
      MovieDto.builder().movId(4).title("The Goodfather").genere("Drama")
          .premiere(OffsetDateTime.parse("1972-03-15T00:00:00+02:00").withNano(0).toString()).rate(9).length(175)
          .country("USA").act(true).description("Test of test description").build(),
      MovieDto.builder().movId(5).title("12 Angry Men").genere("Court drama")
          .premiere(OffsetDateTime.parse("1957-04-10T00:00:00+02:00").withNano(0).toString()).rate(9).length(96)
          .country("USA").act(false).build() };

  private CommandHandler<MoviesFilter, PageDto> handler;

  @Mock
  private CountryRepo countryRepo;

  @Mock
  private MovieRepo movieRepo;

  @BeforeEach
  public void setup() {
    mockedMovies[0].setMovId(1);
    mockedMovies[0].setDescription("Test description");
    mockedMovies[0].setAct(false);

    mockedMovies[1].setMovId(2);

    mockedMovies[2].setMovId(3);

    mockedMovies[3].setMovId(4);
    mockedMovies[3].setDescription("Test of test description");

    mockedMovies[4].setMovId(5);
    mockedMovies[4].setAct(false);

    MockitoAnnotations.initMocks(this);
    handler = new BadGetMovies(countryRepo, movieRepo);
  }

  @Test
  public void emptyPageTest() {
    MoviesFilter filter = new MoviesFilter(null, null, null, null, 0, 0);
    GetMoviesCmd command = new GetMoviesCmd(filter);
    command = (GetMoviesCmd) handler.handle(command);
    Assertions.assertEquals(0, command.getOutput().getContent().size());
  }

  @Test
  public void paginatedTest() {
    MoviesFilter filter = new MoviesFilter(null, null, null, null, 1, 2);
    Mockito.when(movieRepo.findByMoviesFilterAlsoDeleted(filter)).thenReturn(new LinkedList<Movie>() {
      {
        add(mockedMovies[0]);
        add(mockedMovies[1]);
      }
    });
    Mockito.when(movieRepo.countByMoviesFilterAlsoDeleted(filter)).thenReturn(5l);
    GetMoviesCmd command = new GetMoviesCmd(filter);
    command = (GetMoviesCmd) handler.handle(command);
    Assertions.assertEquals(1, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      MovieDto movie = (MovieDto) command.getOutput().getContent().get(i);
      MovieDto mockedMovie = mockedMoviesDto[i];
      Assertions.assertEquals(mockedMovie.getMovId(), movie.getMovId());
      Assertions.assertEquals(mockedMovie.getTitle(), movie.getTitle());
      Assertions.assertEquals(mockedMovie.getGenere(), movie.getGenere());
      Assertions.assertEquals(mockedMovie.getDescription(), movie.getDescription());
      Assertions.assertEquals(mockedMovie.getPremiere(), movie.getPremiere());
      Assertions.assertEquals(mockedMovie.getRate(), movie.getRate());
      Assertions.assertEquals(mockedMovie.getLength(), movie.getLength());
      Assertions.assertEquals(mockedMovie.getCountry(), movie.getCountry());
      Assertions.assertEquals(mockedMovie.getAct(), movie.getAct());
    }

    filter = new MoviesFilter(null, null, null, null, 2, 2);
    Mockito.when(movieRepo.findByMoviesFilterAlsoDeleted(filter)).thenReturn(new LinkedList<Movie>() {
      {
        add(mockedMovies[2]);
        add(mockedMovies[3]);
      }
    });
    Mockito.when(movieRepo.countByMoviesFilterAlsoDeleted(filter)).thenReturn(5l);
    command = new GetMoviesCmd(filter);
    command = (GetMoviesCmd) handler.handle(command);
    Assertions.assertEquals(2, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      MovieDto movie = (MovieDto) command.getOutput().getContent().get(i);
      MovieDto mockedMovie = mockedMoviesDto[i + 2];
      Assertions.assertEquals(mockedMovie.getMovId(), movie.getMovId());
      Assertions.assertEquals(mockedMovie.getTitle(), movie.getTitle());
      Assertions.assertEquals(mockedMovie.getGenere(), movie.getGenere());
      Assertions.assertEquals(mockedMovie.getDescription(), movie.getDescription());
      Assertions.assertEquals(mockedMovie.getPremiere(), movie.getPremiere());
      Assertions.assertEquals(mockedMovie.getRate(), movie.getRate());
      Assertions.assertEquals(mockedMovie.getLength(), movie.getLength());
      Assertions.assertEquals(mockedMovie.getCountry(), movie.getCountry());
      Assertions.assertEquals(mockedMovie.getAct(), movie.getAct());
    }

    filter = new MoviesFilter(null, null, null, null, 3, 2);
    Mockito.when(movieRepo.findByMoviesFilterAlsoDeleted(filter)).thenReturn(new LinkedList<Movie>() {
      {
        add(mockedMovies[4]);
      }
    });
    Mockito.when(movieRepo.countByMoviesFilterAlsoDeleted(filter)).thenReturn(5l);
    command = new GetMoviesCmd(filter);
    command = (GetMoviesCmd) handler.handle(command);
    Assertions.assertEquals(3, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      MovieDto movie = (MovieDto) command.getOutput().getContent().get(i);
      MovieDto mockedMovie = mockedMoviesDto[i + 4];
      Assertions.assertEquals(mockedMovie.getMovId(), movie.getMovId());
      Assertions.assertEquals(mockedMovie.getTitle(), movie.getTitle());
      Assertions.assertEquals(mockedMovie.getGenere(), movie.getGenere());
      Assertions.assertEquals(mockedMovie.getDescription(), movie.getDescription());
      Assertions.assertEquals(mockedMovie.getPremiere(), movie.getPremiere());
      Assertions.assertEquals(mockedMovie.getRate(), movie.getRate());
      Assertions.assertEquals(mockedMovie.getLength(), movie.getLength());
      Assertions.assertEquals(mockedMovie.getCountry(), movie.getCountry());
      Assertions.assertEquals(mockedMovie.getAct(), movie.getAct());
    }
  }

}
