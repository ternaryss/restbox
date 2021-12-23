package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Invalid new actor addition.
 *
 * @author TSS
 */
@Slf4j
public class BadAddActor extends CommandHandler<PersonDto, Integer> {

  private final PersonRepo personRepo;

  public BadAddActor(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  protected PersonDto getInput(Cmd<?, ?> command) {
    if (command instanceof AddActorCmd) {
      return ((AddActorCmd) command).getInput();
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  protected void setOutput(Cmd<?, ?> command, Integer output) {
    if (command instanceof AddActorCmd) {
      ((AddActorCmd) command).setOutput(output);
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    PersonDto input = getInput(command);
    log.info("Adding actor [firstName = {}, lastName = {}]", input.getFirstName(), input.getLastName());

    OffsetDateTime birthday = OffsetDateTime.parse(input.getBirthday());
    Person actor = new Person(input.getFirstName().trim(), input.getLastName().trim(), birthday, 0, false);
    actor.setSecondName(input.getFirstName().trim());

    actor = personRepo.save(actor);
    setOutput(command, actor.getPerId());
    log.info("Actor added [perId = {}]", actor.getPerId());

    return super.handle(command);
  }

}
