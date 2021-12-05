package pl.tss.restbox.core.domain.command.movie;

import org.springframework.data.domain.jaxb.SpringDataJaxb.PageDto;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.filter.MoviesFilter;

/**
 * Get movies command.
 *
 * @author TSS
 */
public class GetMoviesCmd extends Cmd<MoviesFilter, PageDto> {

  public GetMoviesCmd() {
    super(new MoviesFilter());
  }

  public GetMoviesCmd(MoviesFilter filter) {
    super(filter);
  }

}
