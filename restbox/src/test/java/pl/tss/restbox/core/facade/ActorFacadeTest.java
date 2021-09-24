package pl.tss.restbox.core.facade;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
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
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    actorFacade = new ActorFacade(env, personRepo);
  }

  @Test
  public void executeUnknownCommandTest() {
    Cmd<Void, Void> command = new Cmd<Void, Void>() {
    };
    Assertions.assertThrows(UnsupportedOperationException.class, () -> actorFacade.execute(command));
  }

  @Test
  public void addActorTest() {
    PersonDto invalidActor = PersonDto.builder().build();
    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new AddActorCmd(invalidActor)));

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new AddActorCmd(invalidActor)));

    PersonDto validActor = PersonDto.builder().firstName("Jan").lastName("Kowalski")
        .birthday("1996-08-04T00:00:00+02:00").rate(10).build();
    Person person = new Person(validActor.getFirstName(), validActor.getLastName(),
        OffsetDateTime.parse(validActor.getBirthday()), validActor.getRate(), false);
    person.setPerId(1);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(person);

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Assertions.assertEquals(1, ((AddActorCmd) actorFacade.execute(new AddActorCmd(validActor))).getOutput());

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Assertions.assertEquals(1, ((AddActorCmd) actorFacade.execute(new AddActorCmd(validActor))).getOutput());
  }

}
