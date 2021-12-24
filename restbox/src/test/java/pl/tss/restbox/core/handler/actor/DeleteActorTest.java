package pl.tss.restbox.core.handler.actor;

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

import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for actor deletion.
 *
 * @author TSS
 */
public class DeleteActorTest {

  private final Person mockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10,
      false);
  private final Actor[] mockedRolesAssignemnt = new Actor[] { new Actor(mockedActor, null),
      new Actor(mockedActor, null), new Actor(mockedActor, null), new Actor(mockedActor, null) };

  private CommandHandler<Integer, Void> handler;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedRolesAssignemnt[0].setActId(1);
    mockedRolesAssignemnt[1].setActId(2);
    mockedRolesAssignemnt[2].setActId(3);
    mockedRolesAssignemnt[3].setActId(4);

    mockedActor.setPerId(1);
    mockedActor.setActors(Arrays.asList(mockedRolesAssignemnt));

    MockitoAnnotations.initMocks(this);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.save(mockedActor)).thenAnswer(new Answer<Person>() {

      @Override
      public Person answer(InvocationOnMock invocation) throws Throwable {
        mockedActor.setAct(false);

        return mockedActor;
      }

    });

    handler = new DeleteActor(actorRepo, personRepo);
  }

  @Test
  public void deleteActorTest() {
    handler.handle(new DeleteActorCmd(1));
    Assertions.assertFalse(mockedActor.isAct());

    for (Actor roleAssignment : mockedActor.getActors()) {
      Assertions.assertFalse(roleAssignment.isAct());
    }
  }

}
