package pl.tss.restbox.core.handler.movie;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Invalid validation if movie exists (deleted movies are ok).
 *
 * @author TSS
 */
@Slf4j
public class BadValidateMovieExists extends CommandHandler {

  private final MovieRepo movieRepo;

  public BadValidateMovieExists(MovieRepo movieRepo) {
    this.movieRepo = movieRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer input = null;
    List<ApiErrDetails> errors = new LinkedList<>();

    if (command instanceof EditMovieCmd) {
      input = ((EditMovieCmd) command).getInput().getMovId();
    } else {
      input = (Integer) command.getInput();
    }

    log.info("Validating if movie exists [movId = {}]", input);

    Movie movie = movieRepo.findFirstByMovIdAlsoDeleted(input);

    if (movie == null) {
      errors.add(ApiErrDetails.builder().field("movId").message("err.movie.exists").build());
    }

    log.info("Validation if movie exists finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
