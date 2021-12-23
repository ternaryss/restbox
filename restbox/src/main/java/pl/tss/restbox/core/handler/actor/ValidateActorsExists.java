package pl.tss.restbox.core.handler.actor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Validate if actors exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateActorsExists extends CommandHandler<Set<Integer>, Void> {

  private final PersonRepo personRepo;

  private String field = "perId[{}]";

  public ValidateActorsExists(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  protected Set<Integer> getInput(Cmd<?, ?> command) {
    if (command instanceof AddMovieCmd) {
      field = "movie.actors.perId[{}]";

      return ((AddMovieCmd) command).getInput().getActors().stream().map(PersonDto::getPerId)
          .collect(Collectors.toSet());
    } else if (command instanceof EditMovieCmd) {
      field = "movie.actors.perId[{}]";

      return ((EditMovieCmd) command).getInput().getActors().stream().map(PersonDto::getPerId)
          .collect(Collectors.toSet());
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
    Set<Integer> input = getInput(command);
    List<ApiErrDetails> errors = new LinkedList<>();
    log.info("Validating if actors exists [perIds size = {}]", input.size());

    Integer[] identifiers = input.toArray(new Integer[input.size()]);

    for (int i = 0; i < identifiers.length; i++) {
      Person actor = personRepo.findFirstByPerIdAndDirector(identifiers[i], false);

      if (actor == null) {
        errors.add(ApiErrDetails.builder().field(field.replace("{}", String.valueOf(i + 1))).message("err.actor.exists")
            .build());
      }
    }

    log.info("Validation if actors exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
