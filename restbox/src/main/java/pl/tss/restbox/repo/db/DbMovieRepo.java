package pl.tss.restbox.repo.db;

import java.time.OffsetDateTime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Database implementation of movie data repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbMovieRepo implements MovieRepo {

  private final CrudMovieRepo repo;

  public DbMovieRepo(CrudMovieRepo repo) {
    this.repo = repo;
  }

  @Override
  public Movie save(Movie movie) {
    log.debug("Saving movie [movId = {}]", movie.getMovId());
    movie.setModifyDate(OffsetDateTime.now());
    movie = repo.save(movie);
    log.debug("Movie saved [movId = {}]", movie.getMovId());

    return movie;
  }

  /**
   * Nested movie repository.
   */
  private interface CrudMovieRepo extends CrudRepository<Movie, Integer> {

  }

}
