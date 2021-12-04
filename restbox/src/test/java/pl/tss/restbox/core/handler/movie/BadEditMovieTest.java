package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
 * Unit tests for invalid movie edition.
 *
 * @author TSS
 */
public class BadEditMovieTest {

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

  private final List<Actor> mockedRolesAssignment = new LinkedList<Actor>() {
    {
      add(new Actor(mockedActors.get(0), mockedMovie));
      add(new Actor(mockedActors.get(1), mockedMovie));
    }
  };

  PersonDto mockedDirectorPayload = PersonDto.builder().perId(1).build();

  List<PersonDto> mockedActorsPayload = new LinkedList<PersonDto>() {
    {
      add(PersonDto.builder().perId(2).build());
      add(PersonDto.builder().perId(3).build());
    }
  };

  MovieDetailsDto mockedMoviePayload = MovieDetailsDto.builder().title("The Wanted").genere(mockedGenere.getName())
      .description(mockedMovie.getDescription()).premiere("2010-06-12T00:00:00+02:00").rate(9).length(120)
      .country(mockedCountry.getName()).director(mockedDirectorPayload).actors(mockedActorsPayload).build();

  private final Movie mockedModifiedMovie = new Movie("The Wanted", OffsetDateTime.parse("2010-06-12T00:00:00+02:00"),
      9, 120, mockedDirector, mockedCountry, mockedGenere);

  private final List<Actor> mockedModifiedRolesAssignment = new LinkedList<Actor>() {
    {
      add(new Actor(mockedActors.get(0), mockedModifiedMovie));
      add(new Actor(mockedActors.get(1), mockedModifiedMovie));
    }
  };

  private CommandHandler handler;

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
    mockedCountry.setCouId(1);

    mockedGenere.setGenId(1);

    mockedDirector.setPerId(1);

    mockedActors.get(0).setPerId(2);
    mockedActors.get(1).setPerId(3);

    mockedRolesAssignment.get(0).setActId(1);
    mockedRolesAssignment.get(0).setAct(false);
    mockedRolesAssignment.get(1).setActId(2);
    mockedRolesAssignment.get(1).setAct(false);

    mockedMovie.setMovId(1);
    mockedMovie.setAct(false);
    mockedMovie.setActors(mockedRolesAssignment);

    mockedMoviePayload.setMovId(1);

    mockedModifiedRolesAssignment.get(0).setActId(1);
    mockedModifiedRolesAssignment.get(0).setAct(false);
    mockedModifiedRolesAssignment.get(1).setActId(2);
    mockedModifiedRolesAssignment.get(1).setAct(false);

    mockedModifiedMovie.setMovId(1);
    mockedModifiedMovie.setAct(false);
    mockedModifiedMovie.setActors(mockedModifiedRolesAssignment);

    MockitoAnnotations.initMocks(this);
    Mockito.when(movieRepo.findFirstByMovIdAlsoDeleted(mockedMovie.getMovId())).thenReturn(mockedMovie);
    Mockito.when(countryRepo.findFirstByNameIgnoreCase(mockedCountry.getName())).thenReturn(mockedCountry);
    Mockito.when(genereRepo.findFirstByNameIgnoreCase(mockedGenere.getName())).thenReturn(mockedGenere);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(mockedDirector.getPerId(), mockedDirector.isDirector()))
        .thenReturn(mockedDirector);
    Mockito.when(personRepo.findByPerIdInAndDirector(Mockito.anyListOf(Integer.class), Mockito.anyBoolean()))
        .thenReturn(mockedActors);
    Mockito.when(movieRepo.save(Mockito.any())).thenReturn(mockedModifiedMovie);
    Mockito.when(actorRepo.findByMovie(mockedMovie)).thenReturn(mockedRolesAssignment);

    handler = new BadEditMovie(actorRepo, countryRepo, genereRepo, movieRepo, personRepo);
  }

  @Test
  public void editMovieTest() {
    EditMovieCmd command = new EditMovieCmd(mockedMoviePayload);
    command = (EditMovieCmd) handler.handle(command);

    MovieDetailsDto processedMovie = command.getOutput();
    Assertions.assertEquals(mockedModifiedMovie.getMovId(), processedMovie.getMovId());
    Assertions.assertEquals(mockedModifiedMovie.getTitle(), processedMovie.getTitle());
    Assertions.assertEquals(mockedModifiedMovie.getGenere().getName(), processedMovie.getGenere());
    Assertions.assertEquals(mockedModifiedMovie.getDescription(), processedMovie.getDescription());
    Assertions.assertEquals(mockedModifiedMovie.getPremiere().withNano(0).toString(), processedMovie.getPremiere());
    Assertions.assertEquals(mockedModifiedMovie.getRate(), processedMovie.getRate());
    Assertions.assertEquals(mockedModifiedMovie.getLength(), processedMovie.getLength());
    Assertions.assertEquals(mockedModifiedMovie.getCountry().getName(), processedMovie.getCountry());
    Assertions.assertEquals(mockedModifiedMovie.isAct(), processedMovie.getAct());

    PersonDto processedDirecotr = processedMovie.getDirector();
    Assertions.assertEquals(mockedDirector.getPerId(), processedDirecotr.getPerId());
    Assertions.assertEquals(mockedDirector.getFirstName(), processedDirecotr.getFirstName());
    Assertions.assertEquals(mockedDirector.getSecondName(), processedDirecotr.getSecondName());
    Assertions.assertEquals(mockedDirector.getLastName(), processedDirecotr.getLastName());
    Assertions.assertEquals(mockedDirector.getBirthday().withNano(0).toString(), processedDirecotr.getBirthday());
    Assertions.assertEquals(mockedDirector.getAge(), processedDirecotr.getAge());
    Assertions.assertEquals(mockedDirector.getRate(), processedDirecotr.getRate());
    Assertions.assertEquals(mockedDirector.isAct(), processedDirecotr.getAct());

    List<PersonDto> processedActors = processedMovie.getActors();
    Assertions.assertEquals(0, processedActors.size());
  }

}
