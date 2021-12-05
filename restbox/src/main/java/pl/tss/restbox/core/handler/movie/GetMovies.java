package pl.tss.restbox.core.handler.movie;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.GetMoviesCmd;
import pl.tss.restbox.core.domain.dto.MovieDto;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.filter.MoviesFilter;
import pl.tss.restbox.core.domain.filter.Pagination;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Get movies.
 *
 * @author TSS
 */
@Slf4j
public class GetMovies extends CommandHandler {

  private final MovieRepo movieRepo;

  public GetMovies(MovieRepo movieRepo) {
    this.movieRepo = movieRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    MoviesFilter filter = ((GetMoviesCmd) command).getInput();
    Pagination pagination = filter.getPagination();
    List<MovieDto> moviesDto = new LinkedList<>();
    PageDto page = null;
    log.info(
        "Getting movies for filter [title = {}, genere = {}, country = {}, rate = {}, page = {}, size = {}, sort size = {}]",
        filter.getTitle(), filter.getGenere(), filter.getCountry(), filter.getRate(), pagination.getPage(),
        pagination.getSize(), filter.getSort().size());

    if ((pagination.getPage() <= 0) || (pagination.getSize() <= 0)) {
      page = pagination.generatePage(0, moviesDto);
    } else {
      List<Movie> movies = movieRepo.findByMoviesFilter(filter);
      long countedMovies = movieRepo.countByMoviesFilter(filter);

      for (Movie movie : movies) {
        moviesDto.add(MovieDto.builder().movId(movie.getMovId()).title(movie.getTitle())
            .genere(movie.getGenere().getName()).description(movie.getDescription())
            .premiere(movie.getPremiere().withNano(0).toString()).rate(movie.getRate()).length(movie.getLength())
            .country(movie.getCountry().getName()).act(movie.isAct()).build());
      }

      page = pagination.generatePage(countedMovies, moviesDto);
    }

    ((GetMoviesCmd) command).setOutput(page);
    log.info("Movies for filter got [movies size = {}]", moviesDto.size());

    return super.handle(command);
  }

}
