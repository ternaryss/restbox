package pl.tss.restbox.core.facade;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.handler.actor.AddActor;
import pl.tss.restbox.core.handler.actor.BadAddActor;
import pl.tss.restbox.core.handler.actor.ValidateNewActor;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Facade for actors area.
 *
 * @author TSS
 */
@Slf4j
@Service
public class ActorFacade extends Facade {

  private final Environment env;
  private final PersonRepo personRepo;

  public ActorFacade(Environment env, PersonRepo personRepo) {
    this.env = env;
    this.personRepo = personRepo;
  }

  private Cmd<?, ?> addActor(AddActorCmd command) {
    CommandHandler h1 = new ValidateNewActor();
    CommandHandler h2 = null;

    if (super.isValidProfile(env.getActiveProfiles())) {
      h2 = new AddActor(personRepo);
    } else {
      h2 = new BadAddActor(personRepo);
    }

    h1.setNext(h2);

    return h1.handle(command);
  }

  @Override
  public Cmd<?, ?> execute(Cmd<?, ?> command) {
    log.info("Executing actor command [command = {}]", command != null ? command.getClass().getSimpleName() : null);

    if (command instanceof AddActorCmd) {
      return addActor((AddActorCmd) command);
    } else {
      throw new UnsupportedOperationException("Unknown actor command");
    }
  }

}
