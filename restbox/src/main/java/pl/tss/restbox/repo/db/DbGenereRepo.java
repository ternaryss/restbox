package pl.tss.restbox.repo.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.port.output.repo.GenereRepo;

/**
 * Database implementation of genere repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbGenereRepo implements GenereRepo {

  private final CrudGenereRepo repo;

  public DbGenereRepo(CrudGenereRepo repo) {
    this.repo = repo;
  }

  @Override
  public Genere findFirstByNameIgnoreCase(String name) {
    log.debug("Searching for genere [name = {}]", name);
    Genere genere = repo.findFirstByNameIgnoreCaseAndAct(name != null ? name.trim() : null, true);
    log.debug("Genere found [genId = {}]", genere != null ? genere.getGenId() : null);

    return genere;
  }

  /**
   * Nested genere repository.
   */
  private interface CrudGenereRepo extends CrudRepository<Genere, Integer> {

    Genere findFirstByNameIgnoreCaseAndAct(String name, boolean act);

  }

}
