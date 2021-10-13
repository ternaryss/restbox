package pl.tss.restbox.core.domain.command.actor;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.PersonDto;

/**
 * Edit existing actor command.
 *
 * @author TSS
 */
public class EditActorCmd extends Cmd<PersonDto, PersonDto> {

  public EditActorCmd(PersonDto personDto) {
    super(personDto);
  }

}
