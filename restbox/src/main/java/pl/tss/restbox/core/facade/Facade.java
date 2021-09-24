package pl.tss.restbox.core.facade;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Definition of application facade.
 *
 * @author TSS
 */
@Slf4j
abstract class Facade {

  protected boolean isValidProfile(String[] profiles) {
    log.debug("Checking if application is running in 'valid' profile");
    boolean valid = Arrays.asList(profiles).contains("valid");
    log.debug("Profile checked [valid = {}]", valid);

    return valid;
  }

  public abstract Cmd<?, ?> execute(Cmd<?, ?> command);

}
