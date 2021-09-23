package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;

/**
 * Validate new actor payload.
 *
 * @author TSS
 */
@Slf4j
public class ValidateNewActor extends CommandHandler {

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    PersonDto input = ((AddActorCmd) command).getInput();
    List<ApiErrDetails> errors = new LinkedList<>();
    log.info("Validating new actor data [firstName = {}, lastName = {}]", input.getFirstName(), input.getLastName());

    if (input.getFirstName() == null || input.getFirstName().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("firstName").message("err.actor.first-name.req").build());
    }

    if (input.getLastName() == null || input.getLastName().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("lastName").message("err.actor.last-name.req").build());
    }

    if (input.getBirthday() == null || input.getBirthday().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("birthday").message("err.actor.birthday.req").build());
    } else {
      OffsetDateTime birthday = null;

      try {
        birthday = OffsetDateTime.parse(input.getBirthday());
      } catch (Exception ex) {
        errors.add(ApiErrDetails.builder().field("birthday").message("err.actor.birthday.format").build());
      }

      if (birthday != null && birthday.isAfter(OffsetDateTime.now())) {
        errors.add(ApiErrDetails.builder().field("birthday").message("err.actor.birthday.invalid").build());
      }
    }

    if (input.getRate() == null) {
      errors.add(ApiErrDetails.builder().field("rate").message("err.actor.rate.req").build());
    } else if (input.getRate() < 0 || input.getRate() > 10) {
      errors.add(ApiErrDetails.builder().field("rate").message("err.actor.rate.invalid").build());
    }

    log.info("New actor data validated [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
