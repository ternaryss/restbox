package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.EditMovieCmd;
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
 * Invalid edit movie.
 * 
 * @author TSS
 */
@Slf4j
public class BadEditMovie extends CommandHandler<MovieDetailsDto, MovieDetailsDto> {

  private final ActorRepo actorRepo;
  private final CountryRepo countryRepo;
  private final GenereRepo genereRepo;
  private final MovieRepo movieRepo;
  private final PersonRepo personRepo;

  public BadEditMovie(ActorRepo actorRepo, CountryRepo countryRepo, GenereRepo genereRepo, MovieRepo movieRepo,
      PersonRepo personRepo) {
    this.actorRepo = actorRepo;
    this.countryRepo = countryRepo;
    this.genereRepo = genereRepo;
    this.movieRepo = movieRepo;
    this.personRepo = personRepo;
  }

  @Override
  protected MovieDetailsDto getInput(Cmd<?, ?> command) {
    if (command instanceof EditMovieCmd) {
      return ((EditMovieCmd) command).getInput();
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  protected void setOutput(Cmd<?, ?> command, MovieDetailsDto output) {
    if (command instanceof EditMovieCmd) {
      ((EditMovieCmd) command).setOutput(output);
    } else {
      throw new UnsupportedOperationException("Command not supported by handler");
    }
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    MovieDetailsDto input = getInput(command);
    Movie movie = movieRepo.findFirstByMovIdAlsoDeleted(input.getMovId());
    log.info("Modifying movie [movId = {}]", input.getMovId());

    Country country = countryRepo.findFirstByNameIgnoreCase(input.getCountry().trim());
    Genere genere = genereRepo.findFirstByNameIgnoreCase(input.getGenere().trim());
    Person director = personRepo.findFirstByPerIdAndDirector(input.getDirector().getPerId(), true);
    List<Integer> actorsIdentifiers = input.getActors().stream().map(PersonDto::getPerId).collect(Collectors.toList());
    List<Person> actors = personRepo.findByPerIdInAndDirector(actorsIdentifiers, false);

    movie.setTitle(input.getTitle().trim());
    movie.setPremiere(OffsetDateTime.parse(input.getPremiere()));
    movie.setRate(input.getRate());
    movie.setLength(input.getLength());
    movie.setDirector(director);
    movie.setCountry(country);
    movie.setGenere(genere);

    if (input.getDescription() != null && !input.getDescription().trim().isEmpty()) {
      movie.setDescription(input.getDescription().trim());
    }

    movie = movieRepo.save(movie);

    List<Actor> oldRolesAssignment = actorRepo.findByMovie(movie);
    List<Actor> rolesAssignment = new LinkedList<>();
    actorRepo.deleteAll(oldRolesAssignment);

    for (Person actor : actors) {
      Actor assignment = new Actor(actor, movie);

      if (!movie.isAct()) {
        assignment.setAct(false);
      }

      rolesAssignment.add(assignment);
    }

    actorRepo.saveAll(rolesAssignment);

    List<PersonDto> actorsDto = new LinkedList<>();
    rolesAssignment.forEach(assignment -> {
      if (assignment.isAct()) {
        Person actor = assignment.getPerson();
        PersonDto actorDto = PersonDto.builder().perId(actor.getPerId()).firstName(actor.getFirstName())
            .secondName(actor.getSecondName()).lastName(actor.getLastName())
            .birthday(actor.getBirthday().withNano(0).toString()).age(actor.getAge()).rate(actor.getRate())
            .act(actor.isAct()).build();
        actorsDto.add(actorDto);
      }
    });

    PersonDto directorDto = PersonDto.builder().perId(director.getPerId()).firstName(director.getFirstName())
        .secondName(director.getSecondName()).lastName(director.getLastName())
        .birthday(director.getBirthday().withNano(0).toString()).age(director.getAge()).rate(director.getRate())
        .act(director.isAct()).build();

    MovieDetailsDto movieDto = MovieDetailsDto.builder().movId(movie.getMovId()).title(movie.getTitle())
        .genere(movie.getGenere().getName()).description(movie.getDescription())
        .premiere(movie.getPremiere().withNano(0).toString()).rate(movie.getRate()).length(movie.getLength())
        .country(movie.getCountry().getName()).act(movie.isAct()).director(directorDto).actors(actorsDto).build();

    setOutput(command, movieDto);
    log.info("Movie modified [movId = {}]", movie.getMovId());

    return super.handle(command);
  }

}
