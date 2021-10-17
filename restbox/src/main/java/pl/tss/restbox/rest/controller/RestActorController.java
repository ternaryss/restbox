package pl.tss.restbox.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
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
    log.debug("Actor added [perId = {}]", command.getOutput());

    return ResponseEntity.status(201).body(command.getOutput());
  }

  @Override
  @RequestMapping(path = "/{perId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteActor(@PathVariable(name = "perId") Integer perId) {
    log.debug("Deleting actor [perId = {}]", perId);
    actorFacade.execute(new DeleteActorCmd(perId));
    log.debug("Actor deleted [perId = {}]", perId);

    return ResponseEntity.status(200).build();
  }

  @Override
  @RequestMapping(path = "/{perId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonDto> editActor(@PathVariable(name = "perId") Integer perId,
      @RequestBody PersonDto payload) {
    log.debug("Modifiying actor [perId = {}]", perId);
    payload.setPerId(perId);
    EditActorCmd command = (EditActorCmd) actorFacade.execute(new EditActorCmd(payload));
    log.debug("Actor modifed [perId = {}]", command.getOutput().getPerId());

    return ResponseEntity.status(200).body(command.getOutput());
  }

  @Override
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto> getActors(@RequestParam(name = "firstname", required = false) String firstName,
      @RequestParam(name = "lastname", required = false) String lastName,
      @RequestParam(name = "rate", required = false) Integer rate,
      @RequestParam(name = "page", required = false) Integer page,
      @RequestParam(name = "size", required = false) Integer size,
      @RequestParam(name = "sort", required = false) String sort) {
    log.debug("Getting actors [firstName = {}, lastName = {}, rate = {}, page = {}, size = {}, sort = {}]", firstName,
        lastName, rate, page, size, sort);
    ActorsFilter filter = new ActorsFilter(firstName, lastName, rate, page, size);
    String sortQuery = sort != null ? sort : "";

    if (!sortQuery.contains("perid")) {
      sortQuery = sortQuery + ";perid,asc";
    }

    filter.addSort(sortQuery, PersonDto.SortColumn.values());
    GetActorsCmd command = (GetActorsCmd) actorFacade.execute(new GetActorsCmd(filter));
    log.debug("Actors got [actorsSize = {}]", command.getOutput().getContent().size());

    return ResponseEntity.status(command.getOutput().getContent().size() > 0 ? 200 : 204).body(command.getOutput());
  }

}
