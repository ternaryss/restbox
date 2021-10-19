package pl.tss.restbox.core.facade;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.handler.actor.ValidateActorsExists;
import pl.tss.restbox.core.handler.country.ValidateCountryExists;
import pl.tss.restbox.core.handler.director.ValidateDirectorExists;
import pl.tss.restbox.core.handler.genere.ValidateGenereExists;
import pl.tss.restbox.core.handler.movie.AddMovie;
import pl.tss.restbox.core.handler.movie.BadAddMovie;
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

  private Cmd<?, ?> addMovie(AddMovieCmd command) {
    CommandHandler h1 = new ValidateNewMovie();
    CommandHandler h2 = new ValidateCountryExists(countryRepo);
    CommandHandler h3 = new ValidateGenereExists(genereRepo);
    CommandHandler h4 = new ValidateDirectorExists(personRepo);
    CommandHandler h5 = new ValidateActorsExists(personRepo);
    CommandHandler h6 = null;

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

  @Override
  public Cmd<?, ?> execute(Cmd<?, ?> command) {
    log.info("Executing movie command [command = {}]", command != null ? command.getClass().getSimpleName() : null);

    if (command instanceof AddMovieCmd) {
      return addMovie((AddMovieCmd) command);
    } else {
      throw new UnsupportedOperationException("Unknown movie command");
    }
  }

}
