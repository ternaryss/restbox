package pl.tss.restbox.repo.db;

import java.time.OffsetDateTime;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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

  private final EntityManager entityManager;
  private final CrudMovieRepo repo;

  public DbMovieRepo(EntityManager entityManager, CrudMovieRepo repo) {
    this.entityManager = entityManager;
    this.repo = repo;
  }

  @Override
  public Movie findFirstByMovId(Integer movId) {
    log.debug("Searching for movie [movId = {}]", movId);
    Movie movie = null;
    String query = "select mov from Movie mov left join fetch mov.actors actors where mov.movId = :movId and mov.act = :act "
        + "and actors.act = :act";

    try {
      movie = entityManager.createQuery(query, Movie.class).setParameter("movId", movId).setParameter("act", true)
          .getSingleResult();
    } catch (NoResultException ex) {
      movie = null;
    }

    log.debug("Movie found [movId = {}]", movie != null ? movie.getMovId() : null);

    return movie;
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
