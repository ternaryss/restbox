package pl.tss.restbox.core.facade;

import java.util.Set;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.command.movie.DeleteMovieCmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
import pl.tss.restbox.core.domain.command.movie.GetMovieCmd;
import pl.tss.restbox.core.domain.command.movie.GetMoviesCmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.filter.MoviesFilter;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.handler.actor.ValidateActorsExists;
import pl.tss.restbox.core.handler.country.ValidateCountryExists;
import pl.tss.restbox.core.handler.director.ValidateDirectorExists;
import pl.tss.restbox.core.handler.genere.ValidateGenereExists;
import pl.tss.restbox.core.handler.movie.AddMovie;
import pl.tss.restbox.core.handler.movie.BadAddMovie;
import pl.tss.restbox.core.handler.movie.BadEditMovie;
import pl.tss.restbox.core.handler.movie.BadGetMovies;
import pl.tss.restbox.core.handler.movie.BadValidateMovieExists;
import pl.tss.restbox.core.handler.movie.DeleteMovie;
import pl.tss.restbox.core.handler.movie.EditMovie;
import pl.tss.restbox.core.handler.movie.GetMovie;
import pl.tss.restbox.core.handler.movie.GetMovies;
import pl.tss.restbox.core.handler.movie.ValidateMovieExists;
import pl.tss.restbox.core.handler.movie.ValidateNewMovie;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.CountryRepo;
import pl.tss.restbox.core.port.output.repo.GenereRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Facade for movies area.
 *
 * @author TSS
 */
@Slf4j
@Service
public class MovieFacade extends Facade {

  private final ActorRepo actorRepo;
  private final CountryRepo countryRepo;
  private final GenereRepo genereRepo;
  private final MovieRepo movieRepo;
  private final PersonRepo personRepo;

  public MovieFacade(Environment env, ActorRepo actorRepo, CountryRepo countryRepo, GenereRepo genereRepo,
      MovieRepo movieRepo, PersonRepo personRepo) {
    super(env);
    this.actorRepo = actorRepo;
    this.countryRepo = countryRepo;
    this.genereRepo = genereRepo;
    this.movieRepo = movieRepo;
    this.personRepo = personRepo;
  }

  @Transactional
  private Cmd<?, ?> addMovie(AddMovieCmd command) {
    CommandHandler<MovieDetailsDto, Void> h1 = new ValidateNewMovie();
    CommandHandler<String, Void> h2 = new ValidateCountryExists(countryRepo);
    CommandHandler<String, Void> h3 = new ValidateGenereExists(genereRepo);
    CommandHandler<Integer, Void> h4 = new ValidateDirectorExists(personRepo);
    CommandHandler<Set<Integer>, Void> h5 = new ValidateActorsExists(personRepo);
    CommandHandler<MovieDetailsDto, Integer> h6 = null;

    if (super.isValidProfile()) {
      h6 = new AddMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
    } else {
      h6 = new BadAddMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
    }

    h1.setNext(h2);
    h2.setNext(h3);
    h3.setNext(h4);
    h4.setNext(h5);
    h5.setNext(h6);

    return h1.handle(command);
  }

  @Transactional
  private Cmd<?, ?> deleteMovie(DeleteMovieCmd command) {
    CommandHandler<Integer, Void> h1 = new ValidateMovieExists(movieRepo);
    CommandHandler<Integer, Void> h2 = new DeleteMovie(actorRepo, movieRepo);

    h1.setNext(h2);

    return h1.handle(command);
  }

  @Transactional
  private Cmd<?, ?> editMovie(EditMovieCmd command) {
    CommandHandler<Integer, Void> h1 = null;
    CommandHandler<MovieDetailsDto, Void> h2 = new ValidateNewMovie();
    CommandHandler<String, Void> h3 = new ValidateCountryExists(countryRepo);
    CommandHandler<String, Void> h4 = new ValidateGenereExists(genereRepo);
    CommandHandler<Integer, Void> h5 = new ValidateDirectorExists(personRepo);
    CommandHandler<Set<Integer>, Void> h6 = new ValidateActorsExists(personRepo);
    CommandHandler<MovieDetailsDto, MovieDetailsDto> h7 = null;

    if (super.isValidProfile()) {
      h1 = new ValidateMovieExists(movieRepo);
      h7 = new EditMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
    } else {
      h1 = new BadValidateMovieExists(movieRepo);
      h7 = new BadEditMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
    }

    h1.setNext(h2);
    h2.setNext(h3);
    h3.setNext(h4);
    h4.setNext(h5);
    h5.setNext(h6);
    h6.setNext(h7);

    return h1.handle(command);
  }

  private Cmd<?, ?> getMovie(GetMovieCmd command) {
    CommandHandler<Integer, Void> h1 = new ValidateMovieExists(movieRepo);
    CommandHandler<Integer, MovieDetailsDto> h2 = new GetMovie(movieRepo);

    h1.setNext(h2);

    return h1.handle(command);
  }

  private Cmd<?, ?> getMovies(GetMoviesCmd command) {
    CommandHandler<MoviesFilter, PageDto> h1 = null;

    if (super.isValidProfile()) {
      h1 = new GetMovies(movieRepo);
    } else {
      h1 = new BadGetMovies(countryRepo, movieRepo);
    }

    return h1.handle(command);
  }

  @Override
  public Cmd<?, ?> execute(Cmd<?, ?> command) {
    log.info("Executing movie command [command = {}]", command != null ? command.getClass().getSimpleName() : null);

    if (command instanceof AddMovieCmd) {
      return addMovie((AddMovieCmd) command);
    } else if (command instanceof DeleteMovieCmd) {
      return deleteMovie((DeleteMovieCmd) command);
    } else if (command instanceof EditMovieCmd) {
      return editMovie((EditMovieCmd) command);
    } else if (command instanceof GetMovieCmd) {
      return getMovie((GetMovieCmd) command);
    } else if (command instanceof GetMoviesCmd) {
      return getMovies((GetMoviesCmd) command);
    } else {
      throw new UnsupportedOperationException("Unknown movie command");
    }
  }

}
