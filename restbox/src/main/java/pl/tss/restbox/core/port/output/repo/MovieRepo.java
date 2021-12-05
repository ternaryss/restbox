package pl.tss.restbox.core.port.output.repo;

import java.util.List;

import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.filter.MoviesFilter;

/**
 * Definition of movie data repository.
 *
 * @author TSS
 */
public interface MovieRepo {

  long countByMoviesFilter(MoviesFilter filter);

  long countByMoviesFilterAlsoDeleted(MoviesFilter filter);

  Movie findFirstByMovId(Integer movId);

  Movie findFirstByMovIdAlsoDeleted(Integer movId);

  List<Movie> findByMoviesFilter(MoviesFilter filter);

  List<Movie> findByMoviesFilterAlsoDeleted(MoviesFilter filter);

  Movie save(Movie movie);

}
