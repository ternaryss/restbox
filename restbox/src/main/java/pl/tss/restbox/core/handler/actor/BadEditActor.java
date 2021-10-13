package pl.tss.restbox.core.handler.actor;

import java.time.OffsetDateTime;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Incorrect existing actor edition.
 *
 * @author TSS
 */
@Slf4j
public class BadEditActor extends CommandHandler {

  private final PersonRepo personRepo;

  public BadEditActor(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    PersonDto input = ((EditActorCmd) command).getInput();
    Person actor = personRepo.findFirstByPerIdAndDirector(input.getPerId(), false);
    log.info("Modyfing actor [perId = {}]", actor.getPerId());

    OffsetDateTime birthday = OffsetDateTime.parse(input.getBirthday());
    actor.setFirstName(input.getFirstName().trim());
    actor.setLastName(input.getLastName().trim());
    actor.setRate(input.getRate());
    actor.setBirthday(birthday);
    actor.setAct(false);

    if (input.getSecondName() != null && !input.getSecondName().trim().isEmpty()) {
      actor.setSecondName(input.getSecondName().trim());
    }

    actor = personRepo.save(actor);
    PersonDto actorDto = PersonDto.builder().perId(actor.getPerId()).firstName(actor.getFirstName())
        .secondName(actor.getSecondName()).lastName(actor.getLastName())
        .birthday(actor.getBirthday().withNano(0).toString()).age(actor.getAge()).rate(actor.getRate())
        .act(actor.isAct()).build();
    ((EditActorCmd) command).setOutput(actorDto);
    log.info("Actor modified [perId = {}]", actor.getPerId());

    return super.handle(command);
  }

}
