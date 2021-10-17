package pl.tss.restbox.core.handler.actor;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Delete actor.
 *
 * @author TSS
 */
@Slf4j
public class DeleteActor extends CommandHandler {

  private final ActorRepo actorRepo;
  private final PersonRepo personRepo;

  public DeleteActor(ActorRepo actorRepo, PersonRepo personRepo) {
    this.actorRepo = actorRepo;
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer input = null;

    if (command instanceof DeleteActorCmd) {
      input = ((DeleteActorCmd) command).getInput();
    } else {
      input = (Integer) command.getInput();
    }

    log.info("Deleting actor [perId = {}]", input);

    Person actor = personRepo.findFirstByPerIdAndDirector(input, false);
    List<Actor> rolesAssignment = actor.getActors();

    rolesAssignment.forEach(as -> as.setAct(false));
    actorRepo.saveAll(rolesAssignment);

    actor.setAct(false);
    personRepo.save(actor);
    log.info("Actor deleted [perId = {}]", input);

    return super.handle(command);
  }

}
