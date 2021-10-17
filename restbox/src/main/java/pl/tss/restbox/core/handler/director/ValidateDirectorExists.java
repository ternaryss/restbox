package pl.tss.restbox.core.handler.director;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Validate if director exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateDirectorExists extends CommandHandler {

  private final PersonRepo personRepo;

  public ValidateDirectorExists(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer perId = null;
    String field = "perId";
    List<ApiErrDetails> errors = new LinkedList<>();

    if (command instanceof AddMovieCmd) {
      perId = ((AddMovieCmd) command).getInput().getDirector().getPerId();
      field = "director.perId";
    } else {
      perId = (Integer) command.getInput();
    }

    log.info("Validating if director exists [perId = {}]", perId);

    Person director = personRepo.findFirstByPerIdAndDirector(perId, true);

    if (director == null) {
      errors.add(ApiErrDetails.builder().field(field).message("err.director.exists").build());
    }

    log.info("Validation if country exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
