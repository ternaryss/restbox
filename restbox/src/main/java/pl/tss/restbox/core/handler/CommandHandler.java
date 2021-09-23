package pl.tss.restbox.core.handler;

import lombok.Setter;
import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Application commands handler.
 *
 * @author TSS
 */
public abstract class CommandHandler {

  @Setter
  private CommandHandler next;

  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    if (next != null) {
      command = next.handle(command);
    }

    return command;
  }

}
