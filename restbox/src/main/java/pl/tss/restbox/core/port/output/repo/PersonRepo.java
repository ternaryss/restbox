package pl.tss.restbox.core.port.output.repo;

import pl.tss.restbox.core.domain.entity.Person;

/**
 * Definition of person repository.
 *
 * @author TSS
 */
public interface PersonRepo {

  Person save(Person person);

}
