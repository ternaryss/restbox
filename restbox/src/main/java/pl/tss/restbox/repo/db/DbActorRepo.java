package pl.tss.restbox.repo.db;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.port.output.repo.ActorRepo;

/**
 * Database implementation of actor data repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbActorRepo implements ActorRepo {

  private final CrudActorRepo repo;

  public DbActorRepo(CrudActorRepo repo) {
    this.repo = repo;
  }

  @Override
  @Override
  public List<Actor> findByMovie(Movie movie) {
    log.debug("Searching for roles assignment [movId = {}]", movie.getMovId());
    List<Actor> rolesAssignment = repo.findByMovie(movie);
    log.debug("Roles assignment found [rolesAssignment size = {}]",
        rolesAssignment != null ? rolesAssignment.size() : null);

    return rolesAssignment != null ? rolesAssignment : new ArrayList<>();
  }

  @Override
  public void saveAll(List<Actor> rolesAssignment) {
    log.debug("Saving all roles assignment [rolesAssignment size = {}]", rolesAssignment.size());
    rolesAssignment.forEach(rol -> rol.setModifyDate(OffsetDateTime.now()));
    repo.saveAll(rolesAssignment);
    log.debug("All roles assignment saved [rolesAssignment size = {}]", rolesAssignment.size());
  }

  /**
   * Nested actor repository.
   */
  private interface CrudActorRepo extends CrudRepository<Actor, Integer> {

    List<Actor> findByMovie(Movie movie);

  }

}
