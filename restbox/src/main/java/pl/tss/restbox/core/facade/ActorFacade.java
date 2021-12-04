package pl.tss.restbox.core.facade;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.handler.actor.AddActor;
import pl.tss.restbox.core.handler.actor.BadAddActor;
import pl.tss.restbox.core.handler.actor.BadEditActor;
import pl.tss.restbox.core.handler.actor.BadGetActors;
import pl.tss.restbox.core.handler.actor.DeleteActor;
import pl.tss.restbox.core.handler.actor.EditActor;
import pl.tss.restbox.core.handler.actor.GetActors;
import pl.tss.restbox.core.handler.actor.ValidateActorExists;
import pl.tss.restbox.core.handler.actor.ValidateNewActor;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Facade for actors area.
 *
 * @author TSS
 */
@Slf4j
@Service
public class ActorFacade extends Facade {

  private final ActorRepo actorRepo;
  private final PersonRepo personRepo;

  public ActorFacade(Environment env, ActorRepo actorRepo, PersonRepo personRepo) {
    super(env);
    this.actorRepo = actorRepo;
    this.personRepo = personRepo;
  }

  private Cmd<?, ?> addActor(AddActorCmd command) {
    CommandHandler h1 = new ValidateNewActor();
    CommandHandler h2 = null;

    if (super.isValidProfile()) {
      h2 = new AddActor(personRepo);
    } else {
      h2 = new BadAddActor(personRepo);
    }

    h1.setNext(h2);

    return h1.handle(command);
  }

  @Transactional
  private Cmd<?, ?> deleteActor(DeleteActorCmd command) {
    CommandHandler h1 = new ValidateActorExists(personRepo);
    CommandHandler h2 = new DeleteActor(actorRepo, personRepo);

    if (super.isValidProfile()) {
      h1.setNext(h2);
    }

    return h1.handle(command);
  }

  private Cmd<?, ?> editActor(EditActorCmd command) {
    CommandHandler h1 = new ValidateActorExists(personRepo);
    CommandHandler h2 = new ValidateNewActor();
    CommandHandler h3 = null;

    if (super.isValidProfile()) {
      h3 = new EditActor(personRepo);
    } else {
      h3 = new BadEditActor(actorRepo, personRepo);
    }

    h1.setNext(h2);
    h2.setNext(h3);

    return h1.handle(command);
  }

  private Cmd<?, ?> getActors(GetActorsCmd command) {
    CommandHandler h1 = null;

    if (super.isValidProfile()) {
      h1 = new GetActors(personRepo);
    } else {
      h1 = new BadGetActors(personRepo);
    }

    return h1.handle(command);
  }

  @Override
  public Cmd<?, ?> execute(Cmd<?, ?> command) {
    log.info("Executing actor command [command = {}]", command != null ? command.getClass().getSimpleName() : null);

    if (command instanceof AddActorCmd) {
      return addActor((AddActorCmd) command);
    } else if (command instanceof DeleteActorCmd) {
      return deleteActor((DeleteActorCmd) command);
    } else if (command instanceof EditActorCmd) {
      return editActor((EditActorCmd) command);
    } else if (command instanceof GetActorsCmd) {
      return getActors((GetActorsCmd) command);
    } else {
      throw new UnsupportedOperationException("Unknown actor command");
    }
  }

}
