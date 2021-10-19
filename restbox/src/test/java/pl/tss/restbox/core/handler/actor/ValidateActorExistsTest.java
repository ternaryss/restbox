package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for check if actor exists.
 *
 * @author TSS
 */
public class ValidateActorExistsTest {

  private final Person mockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10,
      false);

  private CommandHandler handler;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedActor.setPerId(1);

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(2, false)).thenReturn(null);

    handler = new ValidateActorExists(personRepo);
  }

  @Test
  public void noErrorsTest() {
    PersonDto modifiedActor = PersonDto.builder().perId(1).firstName("Tom").lastName("Cruise")
        .birthday("1962-07-03T00:00:00+02:00").rate(10).build();
    EditActorCmd command = new EditActorCmd(modifiedActor);
    Assertions.assertDoesNotThrow(() -> handler.handle(command));
  }

  @Test
  public void actorDoNotExistsTest() {
    String field = "perId";
    String msgCode = "err.actor.exists";

    PersonDto modifiedActor = PersonDto.builder().perId(2).firstName("Tom").lastName("Cruise")
        .birthday("1962-07-03T00:00:00+02:00").rate(10).build();
    EditActorCmd command = new EditActorCmd(modifiedActor);
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
