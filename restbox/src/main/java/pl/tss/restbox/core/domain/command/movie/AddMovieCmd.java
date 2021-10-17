package pl.tss.restbox.core.domain.command.movie;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;

/**
 * Add movie command.
 *
 * @author TSS
 */
public class AddMovieCmd extends Cmd<MovieDetailsDto, Integer> {

  public AddMovieCmd(MovieDetailsDto movieDetailsDto) {
    super(movieDetailsDto);
  }

}
