package pl.tss.restbox.core.handler.country;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.CountryRepo;

/**
 * Validate if country exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateCountryExists extends CommandHandler<String, Void> {

  private final CountryRepo countryRepo;

  private String field = "name";

  public ValidateCountryExists(CountryRepo countryRepo) {
    this.countryRepo = countryRepo;
  }

  @Override
  protected String getInput(Cmd<?, ?> command) {
    if (command instanceof AddMovieCmd) {
      field = "country.name";

      return ((AddMovieCmd) command).getInput().getCountry();
    } else if (command instanceof EditMovieCmd) {
      field = "country.name";

      return ((EditMovieCmd) command).getInput().getCountry();
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  protected void setOutput(Cmd<?, ?> command, Void output) {
    throw new UnsupportedOperationException("Command not supported by handler");
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    String name = getInput(command);
    List<ApiErrDetails> errors = new LinkedList<>();
    log.info("Validating if country exists [name = {}]", name);

    Country country = countryRepo.findFirstByNameIgnoreCase(name != null ? name.trim() : null);

    if (country == null) {
      errors.add(ApiErrDetails.builder().field(field).message("err.country.exists").build());
    }

    log.info("Validation if country exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
