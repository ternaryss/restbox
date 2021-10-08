package pl.tss.restbox.core.handler.actor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
import pl.tss.restbox.core.domain.filter.Pagination;
import pl.tss.restbox.core.domain.filter.Sortable;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Invalid get actors data.
 *
 * @author TSS
 */
@Slf4j
public class BadGetActors extends CommandHandler {

  private final PersonRepo personRepo;

  public BadGetActors(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    ActorsFilter filter = ((GetActorsCmd) command).getInput();
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
      filter.getSort().remove(PersonDto.SortColumn.RATE);
      ActorsFilter badFilter = new ActorsFilter(null, filter.getLastName(), filter.getRate(), pagination.getPage(),
          pagination.getSize());

      for (Map.Entry<Sortable, Sortable.Direction> entry : filter.getSort().entrySet()) {
        badFilter.addSort(entry.getKey(), entry.getValue());
      }

      List<Person> actors = personRepo.findByActorsFilter(badFilter);
      long countedActors = personRepo.countByActorsFilter(badFilter);

      for (Person actor : actors) {
        actorsDto.add(PersonDto.builder().perId(actor.getPerId()).firstName(actor.getFirstName())
            .secondName(actor.getSecondName()).lastName(actor.getLastName())
            .birthday(actor.getBirthday().withNano(0).toString()).age(actor.getAge() + 100).rate(actor.getRate())
            .act(actor.isAct()).build());
      }

      page = badFilter.getPagination().generatePage(countedActors, actorsDto);
    }

    ((GetActorsCmd) command).setOutput(page);
    log.info("Actors for filter got [actorsSize = {}]", actorsDto.size());

    return super.handle(command);
  }

}
