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
  public long countByActorsFilter(ActorsFilter filter) {
    log.debug("Counting persons by actors filter [firstName = {}, lastName = {}, rate = {}]", filter.getFirstName(),
        filter.getLastName(), filter.getRate());

    long count = 0;
    TypedQuery<Long> query = null;
    String rawQuery = "select count(actor) from Person actor where actor.act = true and actor.director = false "
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
      count = 0;
    }

    log.debug("Persons by actors filter count [count = {}]", count);

    return count;
  }

  @Override
  public List<Person> findByActorsFilter(ActorsFilter filter) {
    Pagination pagination = filter.getPagination();
    log.debug(
        "Searching for persons by actors filter [firstName = {}, lastName = {}, rate = {}, page = {}, size = {}, sort = {}]",
        filter.getFirstName(), filter.getLastName(), filter.getRate(), pagination.getPage(), pagination.getSize(),
        filter.getSortQuery());

    TypedQuery<Person> query = null;
    String rawQuery = "select actor from Person actor where actor.act = true and actor.director = false "
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
  public Person findFirstByDirectorOrderByPerIdAsc(boolean director) {
    log.debug("Searching for person [director = {}]", director);
    Person person = repo.findFirstByDirectorAndActOrderByPerIdAsc(true, true);
    log.debug("Person found [perId = {}]", person != null ? person.getPerId() : null);

    return person;
  }

  @Override
  public Person findFirstByPerIdAndDirector(Integer perId, boolean director) {
    log.debug("Searching for person [perId = {}, director = {}]", perId, director);
    Person person = null;
    String query = "select distinct per from Person per left join fetch per.actors where per.perId = :perId "
        + "and per.director = :director and per.act = :act";

    try {
      person = entityManager.createQuery(query, Person.class).setParameter("perId", perId)
          .setParameter("director", director).setParameter("act", true).getSingleResult();
    } catch (NoResultException ex) {
      person = null;
    }

    log.debug("Person found [perId = {}]", person != null ? person.getPerId() : null);

    return person;
  }

  @Override
  public List<Person> findByPerIdInAndDirector(List<Integer> ids, boolean director) {
    log.debug("Searching for persons [ids size = {}, director = {}]", ids.size(), director);
    String query = "select distinct per from Person per where per.act = :act and per.director = :director and per.perId in :ids";
    List<Person> persons = entityManager.createQuery(query, Person.class).setParameter("act", true)
        .setParameter("director", director).setParameter("ids", ids).getResultList();
    log.debug("Persons found [persons size = {}]", persons != null ? persons.size() : null);

    return persons != null ? persons : new ArrayList<>();
  }

  @Override
  public Person save(Person person) {
    log.debug("Saving person [perId = {}]", person.getPerId());
    person.setModifyDate(OffsetDateTime.now());
    person = repo.save(person);
    log.debug("Person saved [perId = {}]", person.getPerId());

    return person;
  }

  /**
   * Nested person repository.
   */
  private interface CrudPersonRepo extends CrudRepository<Person, Integer> {

    Person findFirstByDirectorAndActOrderByPerIdAsc(boolean director, boolean act);

  }

}
