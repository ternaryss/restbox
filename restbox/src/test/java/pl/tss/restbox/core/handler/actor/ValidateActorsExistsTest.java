package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

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
 * Validate if actors exists unit tests.
 *
 * @author TSS
 */
public class ValidateActorsExistsTest {

  private final List<Person> mockedActors = new LinkedList<Person>() {
    {
      add(new Person("Jan", "Kowalski", OffsetDateTime.parse("1996-08-04T00:00:00+02:00"), 5, false));
      add(new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10, false));
    }
  };

  private CommandHandler handler;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedActors.get(0).setPerId(1);
    mockedActors.get(1).setPerId(2);

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActors.get(0));
    Mockito.when(personRepo.findFirstByPerIdAndDirector(2, false)).thenReturn(mockedActors.get(1));
    Mockito.when(personRepo.findFirstByPerIdAndDirector(10, false)).thenReturn(null);

    handler = new ValidateActorsExists(personRepo);
  }

  @Test
  public void noErrorsTest() {
    List<PersonDto> actors = new LinkedList<PersonDto>() {
      {
        add(PersonDto.builder().perId(1).build());
        add(PersonDto.builder().perId(2).build());
      }
    };

    MovieDetailsDto movie = MovieDetailsDto.builder().actors(actors).build();
    Assertions.assertDoesNotThrow(() -> handler.handle(new AddMovieCmd(movie)));
  }

  @Test
  public void actorsDoNotExistsTest() {
    String field = "movie.actors.perId[3]";
    String msgCode = "err.actor.exists";

    List<PersonDto> actors = new LinkedList<PersonDto>() {
      {
        add(PersonDto.builder().perId(1).build());
        add(PersonDto.builder().perId(2).build());
        add(PersonDto.builder().perId(10).build());
      }
    };

    MovieDetailsDto movie = MovieDetailsDto.builder().actors(actors).build();
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
