package pl.tss.restbox.core.handler.director;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
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
public class ValidateDirectorExists extends CommandHandler<Integer, Void> {

  private final PersonRepo personRepo;

  private String field = "perId";

  public ValidateDirectorExists(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  protected Integer getInput(Cmd<?, ?> command) {
    if (command instanceof AddMovieCmd) {
      field = "director.perId";

      return ((AddMovieCmd) command).getInput().getDirector().getPerId();
    } else if (command instanceof EditMovieCmd) {
      field = "director.perId";

      return ((EditMovieCmd) command).getInput().getDirector().getPerId();
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
    Integer perId = getInput(command);
    List<ApiErrDetails> errors = new LinkedList<>();

    log.info("Validating if director exists [perId = {}]", perId);

    Person director = personRepo.findFirstByPerIdAndDirector(perId, true);

    if (director == null) {
      errors.add(ApiErrDetails.builder().field(field).message("err.director.exists").build());
    }

    log.info("Validation if director exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
