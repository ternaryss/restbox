package pl.tss.restbox.core.port.output.repo;

import pl.tss.restbox.core.domain.entity.Country;

/**
 * Definition of country repository.
 *
 * @author TSS
 */
public interface CountryRepo {

  Country findFirstByNameIgnoreCase(String name);

}
