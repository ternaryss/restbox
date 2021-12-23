package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.dto.ApiErrDetails;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.handler.CommandHandler;

/**
 * Validate new movie payload.
 *
 * @author TSS
 */
@Slf4j
public class ValidateNewMovie extends CommandHandler<MovieDetailsDto, Void> {

  @Override
  protected MovieDetailsDto getInput(Cmd<?, ?> command) {
    if (command instanceof AddMovieCmd) {
      return ((AddMovieCmd) command).getInput();
    } else if (command instanceof EditMovieCmd) {
      return ((EditMovieCmd) command).getInput();
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  protected void setOutput(Cmd<?, ?> command, Void output) {
    throw new UnsupportedOperationException("Command not supported by handler");
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    MovieDetailsDto input = getInput(command);
    List<ApiErrDetails> errors = new LinkedList<>();
    log.info("Validating new movie data [title = {}, premiere = {}]", input.getTitle(), input.getPremiere());

    if (input.getTitle() == null || input.getTitle().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("title").message("err.movie.title.req").build());
    }

    if (input.getGenere() == null || input.getGenere().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("genere").message("err.movie.genere.req").build());
    }

    if (input.getPremiere() == null || input.getPremiere().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("premiere").message("err.movie.premiere.req").build());
    } else {
      try {
        OffsetDateTime.parse(input.getPremiere());
      } catch (Exception ex) {
        errors.add(ApiErrDetails.builder().field("premiere").message("err.movie.premiere.format").build());
      }
    }

    if (input.getRate() == null) {
      errors.add(ApiErrDetails.builder().field("rate").message("err.movie.rate.req").build());
    } else if (input.getRate() < 0 || input.getRate() > 10) {
      errors.add(ApiErrDetails.builder().field("rate").message("err.movie.rate.invalid").build());
    }

    if (input.getLength() == null) {
      errors.add(ApiErrDetails.builder().field("length").message("err.movie.length.req").build());
    } else if (input.getLength() <= 0) {
      errors.add(ApiErrDetails.builder().field("length").message("err.movie.length.invalid").build());
    }

    if (input.getCountry() == null || input.getCountry().trim().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("country").message("err.movie.country.req").build());
    }

    if (input.getDirector() == null) {
      errors.add(ApiErrDetails.builder().field("director").message("err.movie.director.req").build());
    }

    if (input.getActors() == null || input.getActors().isEmpty()) {
      errors.add(ApiErrDetails.builder().field("actors").message("err.movie.actors.req").build());
    }

    log.info("New movie data validation finished [errors size = {}]", errors.size());

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    return super.handle(command);
  }

}
