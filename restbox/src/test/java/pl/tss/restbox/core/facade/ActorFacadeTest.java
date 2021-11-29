package pl.tss.restbox.core.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for actor facade.
 *
 * @author TSS
 */
public class ActorFacadeTest {

  private ActorFacade actorFacade;

  @Mock
  private Environment env;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    actorFacade = new ActorFacade(env, actorRepo, personRepo);
  }

  @Test
  public void executeUnknownCommandTest() {
    Cmd<Void, Void> command = new Cmd<Void, Void>() {
    };
    Assertions.assertThrows(UnsupportedOperationException.class, () -> actorFacade.execute(command));
  }

}
