package pl.tss.restbox.core.port.output.repo;

import java.util.List;

import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.filter.ActorsFilter;

/**
 * Definition of person repository.
 *
 * @author TSS
 */
public interface PersonRepo {

  long countByActorsFilter(ActorsFilter filter);

  List<Person> findByActorsFilter(ActorsFilter filter);

  Person findFirstByDirectorOrderByPerIdAsc(boolean director);

  Person findFirstByPerIdAndDirector(Integer perId, boolean director);

  List<Person> findByPerIdInAndDirector(List<Integer> ids, boolean director);

  Person save(Person person);

}
