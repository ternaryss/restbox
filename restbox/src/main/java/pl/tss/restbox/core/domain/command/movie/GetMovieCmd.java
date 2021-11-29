package pl.tss.restbox.core.domain.command.movie;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;

/**
 * Get single movie details command.
 *
 * @author TSS
 */
public class GetMovieCmd extends Cmd<Integer, MovieDetailsDto> {

  public GetMovieCmd(Integer movId) {
    super(movId);
  }

}
