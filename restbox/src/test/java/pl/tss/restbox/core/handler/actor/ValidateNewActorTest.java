package pl.tss.restbox.core.handler.actor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;

/**
 * Unit tests for new actor data validation.
 *
 * @author TSS
 */
public class ValidateNewActorTest {

  private PersonDto actor;
  private AddActorCmd command;
  private CommandHandler handler;

  @BeforeEach
  public void setup() {
    actor = PersonDto.builder().firstName("Jan").lastName("Kowalski").birthday("1996-08-04T00:00:00+02:00").rate(10)
        .build();

    command = new AddActorCmd(actor);
    handler = new ValidateNewActor();
  }

  @Test
  public void noErrorsTest() {
    Assertions.assertDoesNotThrow(() -> handler.handle(command));
  }

  @Test
  public void firstNameRequiredTest() {
    String field = "firstName";
    String msgCode = "err.actor.first-name.req";

    actor.setFirstName(null);
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
  public void lastNameRequiredTest() {
    String field = "lastName";
    String msgCode = "err.actor.last-name.req";

    actor.setLastName(null);
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
  public void birthdayRequiredTest() {
    String field = "birthday";
    String msgCode = "err.actor.birthday.req";

    actor.setBirthday(null);
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
  public void birthdayFormatTest() {
    String field = "birthday";
    String msgCode = "err.actor.birthday.format";

    actor.setBirthday("1996-08-04 00:00:00");
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
  public void birthdayInvalidTest() {
    String field = "birthday";
    String msgCode = "err.actor.birthday.invalid";

    actor.setBirthday("2100-08-04T00:00:00+02:00");
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
    String msgCode = "err.actor.rate.req";

    actor.setRate(null);
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
    String msgCode = "err.actor.rate.invalid";

    actor.setRate(-1);
    Assertions.assertThrows(ValidationException.class, () -> handler.handle(command));

    try {
      handler.handle(command);
    } catch (ValidationException ex) {
      ApiErrDetails error = ex.getDetails().get(0);
      Assertions.assertEquals(field, error.getField());
      Assertions.assertEquals(msgCode, error.getMessage());
    }

    actor.setRate(11);
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
