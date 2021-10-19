package pl.tss.restbox.core.handler.movie;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;

/**
 * Validate new movie unit tests.
 *
 * @author TSS
 */
public class ValidateNewMovieTest {

  private List<PersonDto> actors = new LinkedList<>();
  private PersonDto director;
  private MovieDetailsDto movie;
  private AddMovieCmd command;
  private CommandHandler handler;

  @BeforeEach
  public void setup() {
    actors.add(PersonDto.builder().perId(1).build());
    actors.add(PersonDto.builder().perId(2).build());

    director = PersonDto.builder().perId(10).build();

    movie = MovieDetailsDto.builder().title("Wanted").genere("Action").premiere("2008-06-12T00:00:00+02:00").rate(7)
        .length(110).country("USA").director(director).actors(actors).build();

    command = new AddMovieCmd(movie);
    handler = new ValidateNewMovie();
  }

  @Test
  public void noErrorsTest() {
    Assertions.assertDoesNotThrow(() -> handler.handle(command));
  }

  @Test
  public void titleRequiredTest() {
    String field = "title";
    String msgCode = "err.movie.title.req";

    movie.setTitle(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void genereRequiredTest() {
    String field = "genere";
    String msgCode = "err.movie.genere.req";

    movie.setGenere(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void premiereRequiredTest() {
    String field = "premiere";
    String msgCode = "err.movie.premiere.req";

    movie.setPremiere(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void premiereFormatTest() {
    String field = "premiere";
    String msgCode = "err.movie.premiere.format";

    movie.setPremiere("2008-06-12 00:00:00");
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void rateRequiredTest() {
    String field = "rate";
    String msgCode = "err.movie.rate.req";

    movie.setRate(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void rateInvalidTest() {
    String field = "rate";
    String msgCode = "err.movie.rate.invalid";

    movie.setRate(-1);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }

    movie.setRate(11);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void lengthRequiredTest() {
    String field = "length";
    String msgCode = "err.movie.length.req";

    movie.setLength(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void lengthValidTest() {
    String field = "length";
    String msgCode = "err.movie.length.invalid";

    movie.setLength(0);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void countryRequiredTest() {
    String field = "country";
    String msgCode = "err.movie.country.req";

    movie.setCountry(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void directorRequiredTest() {
    String field = "director";
    String msgCode = "err.movie.director.req";

    movie.setDirector(null);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }
  }

  @Test
  public void actorsRequiredTest() {
    String field = "actors";
    String msgCode = "err.movie.actors.req";

    movie.setActors(null);
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
