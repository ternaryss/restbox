package pl.tss.restbox.core.handler.genere;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.GenereRepo;

/**
 * Validate if genere exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateGenereExists extends CommandHandler<String, Void> {

  private final GenereRepo genereRepo;

  private String field = "name";

  public ValidateGenereExists(GenereRepo genereRepo) {
    this.genereRepo = genereRepo;
  }

  @Override
  protected String getInput(Cmd<?, ?> command) {
    if (command instanceof AddMovieCmd) {
      field = "genere.name";

      return ((AddMovieCmd) command).getInput().getGenere();
    } else if (command instanceof EditMovieCmd) {
      field = "genere.name";

      return ((EditMovieCmd) command).getInput().getGenere();
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
    log.info("Validating if genere exists [name = {}]", name);

    Genere genere = genereRepo.findFirstByNameIgnoreCase(name != null ? name.trim() : null);

    if (genere == null) {
      errors.add(ApiErrDetails.builder().field(field).message("err.genere.exists").build());
    }

    log.info("Validation if genere exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
