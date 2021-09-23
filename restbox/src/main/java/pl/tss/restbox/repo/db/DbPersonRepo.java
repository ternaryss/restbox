package pl.tss.restbox.repo.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Database implementation of person repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbPersonRepo implements PersonRepo {

  private final CrudPersonRepo repo;

  public DbPersonRepo(CrudPersonRepo repo) {
    this.repo = repo;
  }

  @Override
  public Person save(Person person) {
    log.debug("Saving person [perId = {}]", person.getPerId());
    person = repo.save(person);
    log.debug("Person saved [perId = {}]", person.getPerId());

    return person;
  }

  /**
   * Nested person repository.
   */
  private interface CrudPersonRepo extends CrudRepository<Person, Integer> {

  }

}
