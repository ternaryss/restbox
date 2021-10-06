package pl.tss.restbox.repo.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
import pl.tss.restbox.core.domain.filter.Pagination;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Database implementation of person repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbPersonRepo implements PersonRepo {

  private final EntityManager entityManager;
  private final CrudPersonRepo repo;

  public DbPersonRepo(EntityManager entityManager, CrudPersonRepo repo) {
    this.entityManager = entityManager;
    this.repo = repo;
  }

  @Override
  public int countByActorsFilter(ActorsFilter filter) {
    log.debug("Counting persons by actors filter [firstName = {}, lastName = {}, rate = {}]", filter.getFirstName(),
        filter.getLastName(), filter.getRate());

    Long count = 0l;
    TypedQuery<Long> query = null;
    String rawQuery = "select count(actor) from Person actor where actor.act = true "
        + "and lower(actor.firstName) like '%' || lower(:firstName) || '%' "
        + "and lower(actor.lastName) like '%' || lower(:lastName) || '%'";

    if (filter.getRate() != null) {
      rawQuery = rawQuery + " and actor.rate = :rate";
      query = entityManager.createQuery(rawQuery, Long.class).setParameter("rate", filter.getRate());
    } else {
      query = entityManager.createQuery(rawQuery, Long.class);
    }

    if (filter.getFirstName() != null) {
      query.setParameter("firstName", filter.getFirstName().trim());
    } else {
      query.setParameter("firstName", "");
    }

    if (filter.getLastName() != null) {
      query.setParameter("lastName", filter.getLastName().trim());
    } else {
      query.setParameter("lastName", "");
    }

    try {
      count = query.getSingleResult();
    } catch (NoResultException ex) {
      count = 0l;
    }

    log.debug("Persons by actors filter count [count = {}]", count);

    return count.intValue();
  }

  @Override
  public List<Person> findByActorsFilter(ActorsFilter filter) {
    Pagination pagination = filter.getPagination();
    log.debug(
        "Searching for persons by actors filter [firstName = {}, lastName = {}, rate = {}, page = {}, size = {}, sort = {}]",
        filter.getFirstName(), filter.getLastName(), filter.getRate(), pagination.getPage(), pagination.getSize(),
        filter.getSortQuery());

    TypedQuery<Person> query = null;
    String rawQuery = "select actor from Person actor where actor.act = true "
        + "and lower(actor.firstName) like '%' || lower(:firstName) || '%' "
        + "and lower(actor.lastName) like '%' || lower(:lastName) || '%'";

    if (filter.getRate() != null) {
      rawQuery = rawQuery + " and actor.rate = :rate";
      rawQuery = rawQuery + filter.getSortQuery();
      query = entityManager.createQuery(rawQuery, Person.class).setParameter("rate", filter.getRate());
    } else {
      rawQuery = rawQuery + filter.getSortQuery();
      query = entityManager.createQuery(rawQuery, Person.class);
    }

    if (filter.getFirstName() != null) {
      query.setParameter("firstName", filter.getFirstName().trim());
    } else {
      query.setParameter("firstName", "");
    }

    if (filter.getLastName() != null) {
      query.setParameter("lastName", filter.getLastName().trim());
    } else {
      query.setParameter("lastName", "");
    }

    List<Person> persons = query.setFirstResult(pagination.getOffset()).setMaxResults(pagination.getLimit())
        .getResultList();

    log.debug("Persons by actors filter found [personsSize = {}]", persons != null ? persons.size() : null);

    return persons != null ? persons : new ArrayList<>();
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
