package pl.tss.restbox.core.domain.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Application command.
 *
 * @author TSS
 */
public abstract class Cmd<I, O> {

  @Getter
  private final I input;

  @Setter
  @Getter
  private O output;

  public Cmd() {
    this.input = null;
  }

  public Cmd(I input) {
    this.input = input;
  }

}
