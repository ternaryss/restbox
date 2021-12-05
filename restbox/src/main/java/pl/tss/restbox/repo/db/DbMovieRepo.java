package pl.tss.restbox.repo.db;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.filter.MoviesFilter;
import pl.tss.restbox.core.domain.filter.Pagination;
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
  public long countByMoviesFilter(MoviesFilter filter) {
    log.debug("Counting movies by movies filter [title = {}, genere = {}, country = {}, rate = {}]", filter.getTitle(),
        filter.getGenere(), filter.getCountry(), filter.getRate());

    long count = 0;
    TypedQuery<Long> query = null;
    String rawQuery = "select count(movie) from Movie movie where movie.act = :act "
        + "and lower(movie.title) like '%' || lower(:title) || '%' "
        + "and lower(movie.genere.name) like '%' || lower(:genere) || '%' "
        + "and lower(movie.country.name) like '%' || lower(:country) || '%'";

    if (filter.getRate() != null) {
      rawQuery = rawQuery + " and movie.rate = :rate";
      query = entityManager.createQuery(rawQuery, Long.class).setParameter("rate", filter.getRate());
    } else {
      query = entityManager.createQuery(rawQuery, Long.class);
    }

    if (filter.getTitle() != null) {
      query.setParameter("title", filter.getTitle().trim());
    } else {
      query.setParameter("title", "");
    }

    if (filter.getGenere() != null) {
      query.setParameter("genere", filter.getGenere().trim());
    } else {
      query.setParameter("genere", "");
    }

    if (filter.getCountry() != null) {
      query.setParameter("country", filter.getCountry().trim());
    } else {
      query.setParameter("country", "");
    }

    query.setParameter("act", true);

    try {
      count = query.getSingleResult();
    } catch (NoResultException ex) {
      count = 0;
    }

    log.debug("Movies by movies filter count [count = {}]", count);

    return count;

  }

  @Override
  public Movie findFirstByMovId(Integer movId) {
    log.debug("Searching for movie [movId = {}]", movId);
    Movie movie = null;
    String query = "select mov from Movie mov left join fetch mov.actors actors where mov.movId = :movId and mov.act = :act";

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
  public Movie findFirstByMovIdAlsoDeleted(Integer movId) {
    log.debug("Searching for movie [movId = {}]", movId);
    Movie movie = null;
    String query = "select mov from Movie mov left join fetch mov.actors actors where mov.movId = :movId";

    try {
      movie = entityManager.createQuery(query, Movie.class).setParameter("movId", movId).getSingleResult();
    } catch (NoResultException ex) {
      movie = null;
    }

    log.debug("Movie found [movId = {}]", movie != null ? movie.getMovId() : null);

    return movie;
  }

  @Override
  public List<Movie> findByMoviesFilter(MoviesFilter filter) {
    Pagination pagination = filter.getPagination();
    log.debug(
        "Searching for movies by movies filter [title = {}, genere = {}, country = {}, rate = {}, page = {}, size = {}, sort = {}]",
        filter.getTitle(), filter.getGenere(), filter.getCountry(), filter.getRate(), pagination.getPage(),
        pagination.getSize(), filter.getSortQuery());

    TypedQuery<Movie> query = null;
    String rawQuery = "select movie from Movie movie where movie.act = :act "
        + "and lower(movie.title) like '%' || lower(:title) || '%' "
        + "and lower(movie.genere.name) like '%' || lower(:genere) || '%' "
        + "and lower(movie.country.name) like '%' || lower(:country) || '%'";

    if (filter.getRate() != null) {
      rawQuery = rawQuery + " and movie.rate = :rate";
      rawQuery = rawQuery + filter.getSortQuery();
      query = entityManager.createQuery(rawQuery, Movie.class).setParameter("rate", filter.getRate());
    } else {
      rawQuery = rawQuery + filter.getSortQuery();
      query = entityManager.createQuery(rawQuery, Movie.class);
    }

    if (filter.getTitle() != null) {
      query.setParameter("title", filter.getTitle().trim());
    } else {
      query.setParameter("title", "");
    }

    if (filter.getGenere() != null) {
      query.setParameter("genere", filter.getGenere().trim());
    } else {
      query.setParameter("genere", "");
    }

    if (filter.getCountry() != null) {
      query.setParameter("country", filter.getCountry().trim());
    } else {
      query.setParameter("country", "");
    }

    query.setParameter("act", true);

    List<Movie> movies = query.setFirstResult(pagination.getOffset()).setMaxResults(pagination.getLimit())
        .getResultList();
    log.debug("Movies by movies filter found [movies size = {}]", movies != null ? movies.size() : null);

    return movies != null ? movies : new ArrayList<>();
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
