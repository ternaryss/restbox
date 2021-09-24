package pl.tss.restbox.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.facade.ActorFacade;
import pl.tss.restbox.core.port.input.controller.ActorController;

/**
 * REST implementation of actor controller.
 *
 * @author TSS
 */
@Slf4j
@RestController
@RequestMapping("/actors")
class RestActorController implements ActorController<ResponseEntity<?>> {

  private final ActorFacade actorFacade;

  public RestActorController(ActorFacade actorFacade) {
    this.actorFacade = actorFacade;
  }

  @Override
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Integer> addActor(@RequestBody PersonDto payload) {
    log.debug("Adding actor [payload = {}]", payload);
    AddActorCmd command = (AddActorCmd) actorFacade.execute(new AddActorCmd(payload));
    log.debug("Actor added [actId = {}]", command.getOutput());

    return ResponseEntity.status(201).body(command.getOutput());
  }

}
