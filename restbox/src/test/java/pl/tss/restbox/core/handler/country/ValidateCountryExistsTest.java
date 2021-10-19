package pl.tss.restbox.core.handler.country;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.CountryRepo;

/**
 * Unit tests for country exists validation.
 *
 * @author TSS
 */
public class ValidateCountryExistsTest {

  private final Country mockedCountry = new Country("Poland");

  private CommandHandler handler;

  @Mock
  private CountryRepo countryRepo;

  @BeforeEach
  public void setup() {
    mockedCountry.setCouId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(countryRepo.findFirstByNameIgnoreCase("Poland")).thenReturn(mockedCountry);
    Mockito.when(countryRepo.findFirstByNameIgnoreCase("USA")).thenReturn(null);

    handler = new ValidateCountryExists(countryRepo);
  }

  @Test
  public void noErrorsTest() {
    MovieDetailsDto movie = MovieDetailsDto.builder().country("Poland").build();
    Assertions.assertDoesNotThrow(() -> handler.handle(new AddMovieCmd(movie)));
  }

  @Test
  public void countryDoNotExistsTest() {
    String field = "country.name";
    String msgCode = "err.country.exists";

    MovieDetailsDto movie = MovieDetailsDto.builder().country("USA").build();
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
