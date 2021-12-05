package pl.tss.restbox.core.port.input.controller;

import pl.tss.restbox.core.domain.dto.MovieDetailsDto;

/**
 * Definition of movie controller.
 *
 * @author TSS
 */
public interface MovieController<R> {

  R addMovie(MovieDetailsDto payload);

  R deleteMovie(Integer movId);

  R editMovie(Integer movId, MovieDetailsDto payload);

  R getMovie(Integer movId);

  R getMovies(String title, String genere, String country, Integer rate, Integer page, Integer size, String sort);

}
