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

  int countByActorsFilter(ActorsFilter filter);

  List<Person> findByActorsFilter(ActorsFilter filter);

  Person save(Person person);

}
