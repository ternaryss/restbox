package pl.tss.restbox.core.port.input.controller;

import pl.tss.restbox.core.domain.dto.MovieDetailsDto;

/**
 * Definition of movie controller.
 *
 * @author TSS
 */
public interface MovieController<R> {

  R addMovie(MovieDetailsDto payload);

}