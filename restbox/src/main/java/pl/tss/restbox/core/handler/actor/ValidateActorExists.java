package pl.tss.restbox.core.handler.actor;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Validate if actor exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateActorExists extends CommandHandler<Integer, Void> {

  private final PersonRepo personRepo;

  public ValidateActorExists(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  protected Integer getInput(Cmd<?, ?> command) {
    if (command instanceof DeleteActorCmd) {
      return ((DeleteActorCmd) command).getInput();
    } else if (command instanceof EditActorCmd) {
      return ((EditActorCmd) command).getInput().getPerId();
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  protected void setOutput(Cmd<?, ?> command, Void output) {
    throw new UnsupportedOperationException("Command not supported by handler");
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer input = getInput(command);
    List<ApiErrDetails> errors = new LinkedList<>();
    log.info("Validating if actor exists [perId = {}]", input);

    Person actor = personRepo.findFirstByPerIdAndDirector(input, false);

    if (actor == null) {
      errors.add(ApiErrDetails.builder().field("perId").message("err.actor.exists").build());
    }

    log.info("Validation if actor exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
