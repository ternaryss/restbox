package pl.tss.restbox.core.handler.director;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for validation if director exists.
 *
 * @author TSS
 */
public class ValidateDirectorExistsTest {

  private final Person mockedDirector = new Person("Jan", "Kowalski", OffsetDateTime.parse("1996-08-04T00:00:00+02:00"),
      5, true);

  private CommandHandler handler;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedDirector.setPerId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, true)).thenReturn(mockedDirector);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(10, true)).thenReturn(null);

    handler = new ValidateDirectorExists(personRepo);
  }

  @Test
  public void noErrorsTest() {
    PersonDto director = PersonDto.builder().perId(1).build();
    MovieDetailsDto movie = MovieDetailsDto.builder().director(director).build();
    Assertions.assertDoesNotThrow(() -> handler.handle(new AddMovieCmd(movie)));
  }

  @Test
  public void directorDoNotExistsTest() {
    String field = "director.perId";
    String msgCode = "err.director.exists";

    PersonDto director = PersonDto.builder().perId(10).build();
    MovieDetailsDto movie = MovieDetailsDto.builder().director(director).build();
    AddMovieCmd command = new AddMovieCmd(movie);
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
