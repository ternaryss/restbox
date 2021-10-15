package pl.tss.restbox.core.port.output.repo;

import java.util.List;

import pl.tss.restbox.core.domain.entity.Actor;

/**
 * Definition of actor data repository.
 *
 * @author TSS
 */
public interface ActorRepo {

  void saveAll(List<Actor> rolesAssignment);

}
