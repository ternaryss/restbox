package pl.tss.restbox.core.port.output.repo;

import java.util.List;

import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Movie;

/**
 * Definition of actor data repository.
 *
 * @author TSS
 */
public interface ActorRepo {

  Actor findFirstByMovieAndPersonPerId(Movie movie, Integer perId);

  void saveAll(List<Actor> rolesAssignment);

}
