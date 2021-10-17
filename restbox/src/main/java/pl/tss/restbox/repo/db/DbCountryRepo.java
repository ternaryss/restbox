package pl.tss.restbox.repo.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.port.output.repo.CountryRepo;

/**
 * Database implementation of country repository.
 *
 * @author TSS
 */
@Slf4j
@Repository
class DbCountryRepo implements CountryRepo {

  private final CrudCountryRepo repo;

  public DbCountryRepo(CrudCountryRepo repo) {
    this.repo = repo;
  }

  @Override
  public Country findFirstByNameIgnoreCase(String name) {
    log.debug("Searching for country [name = {}]", name);
    Country country = repo.findFirstByNameIgnoreCaseAndAct(name, true);
    log.debug("Country found [couId = {}]", country != null ? country.getCouId() : null);

    return country;
  }

  /**
   * Nested country repository.
   */
  private interface CrudCountryRepo extends CrudRepository<Country, Integer> {

    Country findFirstByNameIgnoreCaseAndAct(String name, boolean act);

  }

}
