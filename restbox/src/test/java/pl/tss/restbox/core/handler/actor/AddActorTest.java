package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for new actor addition.
 *
 * @author TSS
 */
public class AddActorTest {

  private PersonDto actor;
  private AddActorCmd command;
  private CommandHandler handler;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    actor = PersonDto.builder().firstName("Jan").lastName("Kowalski").birthday("1996-08-04T00:00:00+02:00").rate(10)
        .build();
    Person person = new Person(actor.getFirstName(), actor.getLastName(), OffsetDateTime.parse(actor.getBirthday()),
        actor.getRate(), false);
    person.setPerId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(person);

    command = new AddActorCmd(actor);
    handler = new AddActor(personRepo);
  }

  @Test
  public void addActorTest() {
    command = (AddActorCmd) handler.handle(command);
    Assertions.assertEquals(1, command.getOutput());
  }

}
