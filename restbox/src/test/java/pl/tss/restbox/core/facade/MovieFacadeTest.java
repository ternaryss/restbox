package pl.tss.restbox.core.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.CountryRepo;
import pl.tss.restbox.core.port.output.repo.GenereRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for movie facade.
 *
 * @author TSS
 */
public class MovieFacadeTest {

  private MovieFacade movieFacade;

  @Mock
  private Environment env;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private CountryRepo countryRepo;

  @Mock
  private GenereRepo genereRepo;

  @Mock
  private MovieRepo movieRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    movieFacade = new MovieFacade(env, actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
  }

  @Test
  public void executeUnknownCommandTest() {
    Cmd<Void, Void> command = new Cmd<Void, Void>() {
    };
    Assertions.assertThrows(UnsupportedOperationException.class, () -> movieFacade.execute(command));
  }

}
