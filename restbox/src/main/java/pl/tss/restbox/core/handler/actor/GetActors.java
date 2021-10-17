package pl.tss.restbox.core.handler.actor;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
import pl.tss.restbox.core.domain.filter.Pagination;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Get actors data.
 *
 * @author TSS
 */
@Slf4j
public class GetActors extends CommandHandler {

  private final PersonRepo personRepo;

  public GetActors(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    ActorsFilter filter = null;

    if (command instanceof GetActorsCmd) {
      filter = ((GetActorsCmd) command).getInput();
    } else {
      filter = (ActorsFilter) command.getInput();
    }

    Pagination pagination = filter.getPagination();
    List<PersonDto> actorsDto = new LinkedList<>();
    PageDto page = null;
    log.info(
        "Getting actors for filters [firstName = {}, lastName = {}, rate = {}, page = {}, size = {}, sortSize = {}]",
        filter.getFirstName(), filter.getLastName(), filter.getRate(), pagination.getPage(), pagination.getSize(),
        filter.getSort().size());

    if ((pagination.getPage() <= 0) || (pagination.getSize() <= 0)) {
      page = pagination.generatePage(0, actorsDto);
    } else {
      List<Person> actors = personRepo.findByActorsFilter(filter);
      long countedActors = personRepo.countByActorsFilter(filter);

      for (Person actor : actors) {
        actorsDto.add(PersonDto.builder().perId(actor.getPerId()).firstName(actor.getFirstName())
            .secondName(actor.getSecondName()).lastName(actor.getLastName())
            .birthday(actor.getBirthday().withNano(0).toString()).age(actor.getAge()).rate(actor.getRate())
            .act(actor.isAct()).build());
      }

      page = filter.getPagination().generatePage(countedActors, actorsDto);
    }

    ((GetActorsCmd) command).setOutput(page);
    log.info("Actors for filter got [actorsSize = {}]", actorsDto.size());

    return super.handle(command);
  }

}
