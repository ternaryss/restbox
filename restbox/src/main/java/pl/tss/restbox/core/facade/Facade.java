package pl.tss.restbox.core.facade;

import java.util.Arrays;

import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Definition of application facade.
 *
 * @author TSS
 */
@Slf4j
abstract class Facade {

  private final Environment env;

  public Facade(Environment env) {
    this.env = env;
  }

  protected boolean isValidProfile() {
    log.debug("Checking if application is running in 'valid' profile");
    String[] profiles = env.getActiveProfiles();
    boolean valid = Arrays.asList(profiles).contains("valid");
    log.debug("Profile checked [valid = {}]", valid);

    return valid;
  }

  public abstract Cmd<?, ?> execute(Cmd<?, ?> command);

}
