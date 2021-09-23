package pl.tss.restbox.core.facade;

import java.util.Arrays;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Definition of application facade.
 *
 * @author TSS
 */
abstract class Facade {

  protected boolean isValidProfile(String[] profiles) {
    return Arrays.asList(profiles).contains("valid");
  }

  public abstract Cmd<?, ?> execute(Cmd<?, ?> command);

}
