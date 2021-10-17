package pl.tss.restbox.core.domain.command.actor;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Delete existing actor command.
 *
 * @author TSS
 */
public class DeleteActorCmd extends Cmd<Integer, Void> {

  public DeleteActorCmd(Integer perId) {
    super(perId);
  }

}
