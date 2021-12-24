package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
 * Unit tests for invalid add movie.
 *
 * @author TSS
 */
public class BadAddMovieTest {

  private final Country mockedCountry = new Country("USA");
  private final Genere mockedGenere = new Genere("Action");
  private final Person mockedDirector = new Person("Frank", "Darabont",
      OffsetDateTime.parse("1959-01-28T00:00:00+02:00"), 9, true);
  private final List<Person> mockedActors = new LinkedList<Person>() {
    {
      add(new Person("Jan", "Kowalski", OffsetDateTime.parse("1996-08-04T00:00:00+02:00"), 8, false));
      add(new Person("Alice", "Blooman", OffsetDateTime.parse("1990-09-15T00:00:00+02:00"), 6, false));
    }
  };
  private final Movie mockedMovie = new Movie("Wanted", OffsetDateTime.parse("2008-06-12T00:00:00+02:00"), 7, 110,
      mockedDirector, mockedCountry, mockedGenere);

  private MovieDetailsDto movie;
  private AddMovieCmd command;
  private CommandHandler<MovieDetailsDto, Integer> handler;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private CountryRepo countryRepo;

  @Mock
  private GenereRepo genereRepo;

  @Mock
  private MovieRepo movieRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    List<PersonDto> actors = new LinkedList<>();
    actors.add(PersonDto.builder().perId(1).build());
    actors.add(PersonDto.builder().perId(2).build());

    PersonDto director = PersonDto.builder().perId(10).build();

    movie = MovieDetailsDto.builder().title("Wanted").genere("Action").premiere("2008-06-12T00:00:00+02:00").rate(7)
        .length(110).country("USA").director(director).actors(actors).build();

    mockedCountry.setCouId(1);

    mockedGenere.setGenId(1);

    mockedDirector.setPerId(10);

    mockedActors.get(0).setPerId(1);
    mockedActors.get(1).setPerId(2);

    List<Actor> mockedActorsAssignment = new LinkedList<Actor>() {
      {
        add(new Actor(mockedActors.get(0), mockedMovie));
        add(new Actor(mockedActors.get(1), mockedMovie));
      }
    };
    mockedMovie.setMovId(1);
    mockedMovie.setActors(mockedActorsAssignment);

    MockitoAnnotations.initMocks(this);
    Mockito.when(countryRepo.findFirstByNameIgnoreCase("USA")).thenReturn(mockedCountry);
    Mockito.when(genereRepo.findFirstByNameIgnoreCase("Action")).thenReturn(mockedGenere);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(10, true)).thenReturn(mockedDirector);
    Mockito
        .when(personRepo
            .findByPerIdInAndDirector(mockedActors.stream().map(Person::getPerId).collect(Collectors.toList()), false))
        .thenReturn(mockedActors);
    Mockito.when(movieRepo.save(Mockito.any())).thenReturn(mockedMovie);

    command = new AddMovieCmd(movie);
    handler = new BadAddMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
  }

  @Test
  public void addMovieTest() {
    command = (AddMovieCmd) handler.handle(command);
    Assertions.assertEquals(1, command.getOutput());
  }

}
