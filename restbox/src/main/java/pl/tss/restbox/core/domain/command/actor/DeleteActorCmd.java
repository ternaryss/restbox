package pl.tss.restbox.core.domain.command.actor;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.PersonDto;

/**
 * Delete existing actor command.
 *
 * @author TSS
 */
public class DeleteActorCmd extends Cmd<PersonDto, Void> {

  public DeleteActorCmd(Integer perId) {
    super(PersonDto.builder().perId(perId).build());
  }

}
