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

import pl.tss.restbox.core.domain.command.movie.GetMovieCmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

public class GetMovieTest {

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

  private final PersonDto mockedDirectorDto = PersonDto.builder().perId(1).firstName(mockedDirector.getFirstName())
      .lastName(mockedDirector.getLastName()).birthday(mockedDirector.getBirthday().withNano(0).toString())
      .age(mockedDirector.getAge()).rate(mockedDirector.getRate()).act(true).build();

  private final List<PersonDto> mockedActorsDto = new LinkedList<PersonDto>() {
    {
      add(PersonDto.builder().perId(2).firstName(mockedActors.get(0).getFirstName())
          .lastName(mockedActors.get(0).getLastName())
          .birthday(mockedActors.get(0).getBirthday().withNano(0).toString()).age(mockedActors.get(0).getAge())
          .rate(mockedActors.get(0).getRate()).act(true).build());
      add(PersonDto.builder().perId(3).firstName(mockedActors.get(1).getFirstName())
          .lastName(mockedActors.get(1).getLastName())
          .birthday(mockedActors.get(1).getBirthday().withNano(0).toString()).age(mockedActors.get(1).getAge())
          .rate(mockedActors.get(1).getRate()).act(true).build());
    }
  };

  private final MovieDetailsDto mockedMovieDto = MovieDetailsDto.builder().movId(1).title(mockedMovie.getTitle())
      .genere(mockedGenere.getName()).description(mockedMovie.getDescription())
      .premiere(mockedMovie.getPremiere().withNano(0).toString()).rate(mockedMovie.getRate())
      .length(mockedMovie.getLength()).country(mockedCountry.getName()).act(true).director(mockedDirectorDto)
      .actors(mockedActorsDto).build();

  private CommandHandler handler;

  @Mock
  private MovieRepo movieRepo;

  @BeforeEach
  public void setup() {
    mockedCountry.setCouId(1);

    mockedGenere.setGenId(1);

    mockedDirector.setPerId(1);

    mockedActors.get(0).setPerId(2);
    mockedActors.get(1).setPerId(3);

    mockedMovie.setMovId(1);
    mockedMovie.setActors(mockedRolesAssignment);

    MockitoAnnotations.initMocks(this);
    Mockito.when(movieRepo.findFirstByMovId(1)).thenReturn(mockedMovie);

    handler = new GetMovie(movieRepo);
  }

  @Test
  public void getMovieTest() {
    GetMovieCmd command = new GetMovieCmd(1);
    command = (GetMovieCmd) handler.handle(command);

    MovieDetailsDto processedMovie = command.getOutput();
    Assertions.assertEquals(mockedMovie.getMovId(), processedMovie.getMovId());
    Assertions.assertEquals(mockedMovie.getTitle(), processedMovie.getTitle());
    Assertions.assertEquals(mockedMovie.getGenere().getName(), processedMovie.getGenere());
    Assertions.assertEquals(mockedMovie.getDescription(), processedMovie.getDescription());
    Assertions.assertEquals(mockedMovie.getPremiere().withNano(0).toString(), processedMovie.getPremiere());
    Assertions.assertEquals(mockedMovie.getRate(), processedMovie.getRate());
    Assertions.assertEquals(mockedMovie.getLength(), processedMovie.getLength());
    Assertions.assertEquals(mockedMovie.getCountry().getName(), processedMovie.getCountry());
    Assertions.assertEquals(mockedMovie.isAct(), processedMovie.getAct());

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

    for (int i = 0; i < processedActors.size(); i++) {
      Assertions.assertEquals(mockedActors.get(i).getPerId(), processedActors.get(i).getPerId());
      Assertions.assertEquals(mockedActors.get(i).getFirstName(), processedActors.get(i).getFirstName());
      Assertions.assertEquals(mockedActors.get(i).getSecondName(), processedActors.get(i).getSecondName());
      Assertions.assertEquals(mockedActors.get(i).getLastName(), processedActors.get(i).getLastName());
      Assertions.assertEquals(mockedActors.get(i).getBirthday().withNano(0).toString(),
          processedActors.get(i).getBirthday());
      Assertions.assertEquals(mockedActors.get(i).getAge(), processedActors.get(i).getAge());
      Assertions.assertEquals(mockedActors.get(i).getRate(), processedActors.get(i).getRate());
      Assertions.assertEquals(mockedActors.get(i).isAct(), processedActors.get(i).getAct());
    }
  }

}
