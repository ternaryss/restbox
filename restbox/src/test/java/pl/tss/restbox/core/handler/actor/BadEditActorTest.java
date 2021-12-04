package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for incorrect actor edition.
 *
 * @author TSS
 */
public class BadEditActorTest {

  private final Person mockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10,
      false);
  private final Person modifiedActor = new Person("Mot", "Esiurc", OffsetDateTime.parse("1999-07-03T00:00:00+02:00"), 1,
      false);
  private final PersonDto payload = PersonDto.builder().perId(1).firstName("Mot").secondName("Test").lastName("Esiurc")
      .birthday("1999-07-03T00:00+02:00").rate(1).act(false).build();

  private CommandHandler handler;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedActor.setPerId(1);

    modifiedActor.setPerId(1);
    modifiedActor.setSecondName("Test");
    modifiedActor.setAct(false);

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(modifiedActor);

    handler = new BadEditActor(actorRepo, personRepo);
  }

  @Test
  public void editActorTest() {
    EditActorCmd command = new EditActorCmd(payload);
    command = (EditActorCmd) handler.handle(command);

    Assertions.assertEquals(payload.getPerId(), command.getOutput().getPerId());
    Assertions.assertEquals(payload.getFirstName(), command.getOutput().getFirstName());
    Assertions.assertEquals(payload.getSecondName(), command.getOutput().getSecondName());
    Assertions.assertEquals(payload.getLastName(), command.getOutput().getLastName());
    Assertions.assertEquals(payload.getBirthday(), command.getOutput().getBirthday());
    Assertions.assertEquals(payload.getRate(), command.getOutput().getRate());
    Assertions.assertEquals(payload.getAct(), command.getOutput().getAct());
  }

}
