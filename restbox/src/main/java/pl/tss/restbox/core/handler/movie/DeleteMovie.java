package pl.tss.restbox.core.handler.movie;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.DeleteMovieCmd;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Delete existing movie.
 *
 * @author TSS
 */
@Slf4j
public class DeleteMovie extends CommandHandler<Integer, Void> {

  private final ActorRepo actorRepo;
  private final MovieRepo movieRepo;

  public DeleteMovie(ActorRepo actorRepo, MovieRepo movieRepo) {
    this.actorRepo = actorRepo;
    this.movieRepo = movieRepo;
  }

  @Override
  protected Integer getInput(Cmd<?, ?> command) {
    if (command instanceof DeleteMovieCmd) {
      return ((DeleteMovieCmd) command).getInput();
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
    Integer input = getInput(command);
    log.info("Deleting movie [movId = {}]", input);

    Movie movie = movieRepo.findFirstByMovId(input);
    List<Actor> rolesAssignment = movie.getActors();

    rolesAssignment.forEach(as -> as.setAct(false));
    actorRepo.saveAll(rolesAssignment);

    movie.setAct(false);
    movieRepo.save(movie);
    log.info("Movie deleted [movId = {}]", input);

    return super.handle(command);
  }

}
