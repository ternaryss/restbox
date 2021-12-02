package pl.tss.restbox.core.domain.command.movie;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;

/**
 * Edit existing movie command.
 *
 * @author TSS
 */
public class EditMovieCmd extends Cmd<MovieDetailsDto, MovieDetailsDto> {

  public EditMovieCmd(MovieDetailsDto movieDto) {
    super(movieDto);
  }

}
