package pl.tss.restbox.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.facade.MovieFacade;
import pl.tss.restbox.core.port.input.controller.MovieController;

/**
 * REST implementation of movie controller.
 *
 * @author TSS
 */
@Slf4j
@RestController
@RequestMapping("/movies")
class RestMovieController implements MovieController<ResponseEntity<?>> {

  private final MovieFacade movieFacade;

  public RestMovieController(MovieFacade movieFacade) {
    this.movieFacade = movieFacade;
  }

  @Override
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addMovie(@RequestBody MovieDetailsDto payload) {
    log.debug("Adding movie [payload = {}]", payload);
    AddMovieCmd command = (AddMovieCmd) movieFacade.execute(new AddMovieCmd(payload));
    log.debug("Movie added [movId = {}]", command.getOutput());

    return ResponseEntity.status(201).body(command.getOutput());
  }

}
