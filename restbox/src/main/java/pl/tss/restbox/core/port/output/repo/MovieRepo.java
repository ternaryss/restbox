package pl.tss.restbox.core.port.output.repo;

import pl.tss.restbox.core.domain.entity.Movie;

/**
 * Definition of movie data repository.
 *
 * @author TSS
 */
public interface MovieRepo {

  Movie save(Movie movie);

}
