package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.AddMovieCmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.CountryRepo;
import pl.tss.restbox.core.port.output.repo.GenereRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Invalid add movie.
 *
 * @author TSS
 */
@Slf4j
public class BadAddMovie extends CommandHandler {

  private final ActorRepo actorRepo;
  private final CountryRepo countryRepo;
  private final GenereRepo genereRepo;
  private final MovieRepo movieRepo;
  private final PersonRepo personRepo;

  public BadAddMovie(ActorRepo actorRepo, CountryRepo countryRepo, GenereRepo genereRepo, MovieRepo movieRepo,
      PersonRepo personRepo) {
    this.actorRepo = actorRepo;
    this.countryRepo = countryRepo;
    this.genereRepo = genereRepo;
    this.movieRepo = movieRepo;
    this.personRepo = personRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    MovieDetailsDto input = ((AddMovieCmd) command).getInput();
    log.info("Adding movie [title = {}, premiere = {}]", input.getTitle(), input.getPremiere());

    Country country = countryRepo.findFirstByNameIgnoreCase(input.getCountry().trim());
    Genere genere = genereRepo.findFirstByNameIgnoreCase(input.getGenere().trim());
    Person director = personRepo.findFirstByDirectorOrderByPerIdAsc(true);
    List<Integer> actorsIdentifiers = input.getActors().stream().map(PersonDto::getPerId).collect(Collectors.toList());
    List<Person> actors = personRepo.findByPerIdInAndDirector(actorsIdentifiers, false);
    Movie movie = new Movie(input.getTitle().trim(), OffsetDateTime.parse(input.getPremiere()), input.getLength(),
        input.getLength(), director, country, genere);

    movie = movieRepo.save(movie);
    List<Actor> rolesAssignment = new LinkedList<>();

    for (Person actor : actors) {
      rolesAssignment.add(new Actor(actor, movie));
    }

    actorRepo.saveAll(rolesAssignment);
    ((AddMovieCmd) command).setOutput(movie.getMovId());
    log.info("Movie added [movId = {}]", movie.getMovId());

    return super.handle(command);
  }

}
