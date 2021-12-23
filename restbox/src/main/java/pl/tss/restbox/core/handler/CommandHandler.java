package pl.tss.restbox.core.handler;

import lombok.Setter;
import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Application commands handler.
 *
 * @author TSS
 */
public abstract class CommandHandler<I, O> {

  @Setter
  private CommandHandler<?, ?> next;

  protected abstract I getInput(Cmd<?, ?> command);

  protected abstract void setOutput(Cmd<?, ?> command, O output);

  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    if (next != null) {
      command = next.handle(command);
    }

    return command;
  }

}
