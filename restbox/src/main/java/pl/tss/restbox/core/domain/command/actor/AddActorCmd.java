package pl.tss.restbox.core.domain.command.actor;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.PersonDto;

/**
 * Add actor command.
 *
 * @author TSS
 */
public class AddActorCmd extends Cmd<PersonDto, Integer> {

  public AddActorCmd(PersonDto personDto) {
    super(personDto);
  }

}
