package pl.tss.restbox.core.facade;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Definition of application facade.
 *
 * @author TSS
 */
abstract class Facade {

  public abstract Cmd<?, ?> execute(Cmd<?, ?> command);

}
