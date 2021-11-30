package pl.tss.restbox.core.handler.movie;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Validate if movie exists.
 *
 * @author TSS
 */
@Slf4j
public class ValidateMovieExists extends CommandHandler {

  private final MovieRepo movieRepo;

  public ValidateMovieExists(MovieRepo movieRepo) {
    this.movieRepo = movieRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer movId = (Integer) command.getInput();
    List<ApiErrDetails> errors = new LinkedList<>();

    log.info("Validating if movie exists [movId = {}]", movId);
    Movie movie = movieRepo.findFirstByMovId(movId);

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
