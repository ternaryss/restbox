package pl.tss.restbox.core.handler.country;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
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
public class ValidateCountryExists extends CommandHandler {

  private final CountryRepo countryRepo;

  public ValidateCountryExists(CountryRepo countryRepo) {
    this.countryRepo = countryRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    String name = null;
    List<ApiErrDetails> errors = new LinkedList<>();

    if (command instanceof AddMovieCmd) {
      name = ((AddMovieCmd) command).getInput().getCountry();
    } else {
      name = (String) command.getInput();
    }

    log.info("Validating if country exists [name = {}]", name);

    Country country = countryRepo.findFirstByNameIgnoreCase(name != null ? name.trim() : null);

    if (country == null) {
      errors.add(ApiErrDetails.builder().field("country.name").message("err.country.exists").build());
    }

    log.info("Validation if country exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
