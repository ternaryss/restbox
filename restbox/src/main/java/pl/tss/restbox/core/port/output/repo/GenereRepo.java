package pl.tss.restbox.core.port.output.repo;

import pl.tss.restbox.core.domain.entity.Genere;

/**
 * Definition of genere repository.
 *
 * @author TSS
 */
public interface GenereRepo {

  Genere findFirstByNameIgnoreCase(String name);

}
