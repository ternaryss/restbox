package pl.tss.restbox.core.handler.movie;

import java.time.OffsetDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.tss.restbox.core.domain.command.movie.DeleteMovieCmd;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Country;
import pl.tss.restbox.core.domain.entity.Genere;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Unit tests for existing movie deletion.
 *
 * @author TSS
 */
public class DeleteMovieTest {

  private final Country mockedCountry = new Country("USA");
  private final Genere mockedGenere = new Genere("Action");
  private final Person mockedDirector = new Person("Frank", "Darabont",
      OffsetDateTime.parse("1959-01-28T00:00:00+02:00"), 9, true);
  private final Movie mockedMovie = new Movie("Wanted", OffsetDateTime.parse("2008-06-12T00:00:00+02:00"), 7, 110,
      mockedDirector, mockedCountry, mockedGenere);
  private final Actor[] mockedRolesAssignemnt = new Actor[] { new Actor(null, mockedMovie),
      new Actor(null, mockedMovie), new Actor(null, mockedMovie), new Actor(null, mockedMovie) };

  private CommandHandler handler;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private MovieRepo movieRepo;

  @BeforeEach
  public void setup() {
    mockedRolesAssignemnt[0].setActId(1);
    mockedRolesAssignemnt[1].setActId(2);
    mockedRolesAssignemnt[2].setActId(3);
    mockedRolesAssignemnt[3].setActId(4);

    mockedMovie.setMovId(1);
    mockedMovie.setActors(Arrays.asList(mockedRolesAssignemnt));

    MockitoAnnotations.initMocks(this);
    Mockito.when(movieRepo.findFirstByMovId(1)).thenReturn(mockedMovie);
    Mockito.when(movieRepo.save(mockedMovie)).thenAnswer(new Answer<Movie>() {

      @Override
      public Movie answer(InvocationOnMock invocation) throws Throwable {
        mockedMovie.setAct(false);

        return mockedMovie;
      }

    });

    handler = new DeleteMovie(actorRepo, movieRepo);
  }

  @Test
  public void deleteMovieTest() {
    handler.handle(new DeleteMovieCmd(1));
    Assertions.assertFalse(mockedMovie.isAct());

    for (Actor roleAssignment : mockedMovie.getActors()) {
      Assertions.assertFalse(roleAssignment.isAct());
    }
  }

}
