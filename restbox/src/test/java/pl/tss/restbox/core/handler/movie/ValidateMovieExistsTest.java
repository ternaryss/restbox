package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.movie.GetMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Unit tests for check if movie exists.
 *
 * @author TSS
 */
public class ValidateMovieExistsTest {

  private final Country mockedCountry = new Country("USA");
  private final Genere mockedGenere = new Genere("Action");
  private final Person mockedDirector = new Person("Frank", "Darabont",
      OffsetDateTime.parse("1959-01-28T00:00:00+02:00"), 9, true);
  private final Movie mockedMovie = new Movie("Wanted", OffsetDateTime.parse("2008-06-12T00:00:00+02:00"), 7, 110,
      mockedDirector, mockedCountry, mockedGenere);

  private CommandHandler<Integer, Void> handler;

  @Mock
  private MovieRepo movieRepo;

  @BeforeEach
  public void setup() {
    mockedCountry.setCouId(1);

    mockedGenere.setGenId(1);

    mockedDirector.setPerId(1);

    mockedMovie.setMovId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(movieRepo.findFirstByMovId(1)).thenReturn(mockedMovie);
    Mockito.when(movieRepo.findFirstByMovId(2)).thenReturn(null);

    handler = new ValidateMovieExists(movieRepo);
  }

  @Test
  public void noErrorsTest() {
    GetMovieCmd command = new GetMovieCmd(1);
    Assertions.assertDoesNotThrow(() -> handler.handle(command));
  }

  @Test
  public void movieDoNotExistsTest() {
    String field = "movId";
    String msgCode = "err.movie.exists";

    GetMovieCmd command = new GetMovieCmd(2);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

}
