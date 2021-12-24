package pl.tss.restbox.core.handler.genere;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.GenereRepo;

/**
 * Unit tests to check genere exists validation.
 *
 * @author TSS
 */
public class ValidateGenereExistsTest {

  private final Genere mockedGenere = new Genere("Action");

  private CommandHandler<String, Void> handler;

  @Mock
  private GenereRepo genereRepo;

  @BeforeEach
  public void setup() {
    mockedGenere.setGenId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(genereRepo.findFirstByNameIgnoreCase("Action")).thenReturn(mockedGenere);
    Mockito.when(genereRepo.findFirstByNameIgnoreCase("Thriller")).thenReturn(null);

    handler = new ValidateGenereExists(genereRepo);
  }

  @Test
  public void noErrorsTest() {
    MovieDetailsDto movie = MovieDetailsDto.builder().genere("Action").build();
    Assertions.assertDoesNotThrow(() -> handler.handle(new AddMovieCmd(movie)));
  }

  @Test
  public void genereDoNotExistsTest() {
    String field = "genere.name";
    String msgCode = "err.genere.exists";

    MovieDetailsDto movie = MovieDetailsDto.builder().genere("Thriller").build();
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
