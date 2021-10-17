package pl.tss.restbox.core.facade;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.command.actor.DeleteActorCmd;
import pl.tss.restbox.core.domain.command.actor.EditActorCmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Actor;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
import pl.tss.restbox.core.port.output.repo.ActorRepo;
import pl.tss.restbox.core.port.output.repo.PersonRepo;

/**
 * Unit tests for actor facade.
 *
 * @author TSS
 */
public class ActorFacadeTest {

  private final Person[] mockedActors = new Person[] {
      new Person("Alice", "Blooman", OffsetDateTime.parse("1990-09-15T00:00:00+02:00"), 6, false),
      new Person("John", "Conan", OffsetDateTime.parse("1986-11-11T00:00:00+02:00"), 8, false),
      new Person("Zbigniew", "Stonoga", OffsetDateTime.parse("1978-08-04T00:00:00+02:00"), 9, false),
      new Person("Tom", "Boom", OffsetDateTime.parse("1996-05-01T00:00:00+02:00"), 10, false),
      new Person("Pati", "Anderson", OffsetDateTime.parse("2000-07-20T00:00:00+02:00"), 5, false) };

  private final PersonDto[] mockedActorsDto = new PersonDto[] {
      PersonDto.builder().perId(1).firstName("Alice").lastName("Blooman")
          .birthday(OffsetDateTime.parse("1990-09-15T00:00:00+02:00").withNano(0).toString()).rate(6).build(),
      PersonDto.builder().perId(2).firstName("John").secondName("Lee").lastName("Conan")
          .birthday(OffsetDateTime.parse("1986-11-11T00:00:00+02:00").withNano(0).toString()).rate(8).build(),
      PersonDto.builder().perId(3).firstName("Zbigniew").lastName("Stonoga")
          .birthday(OffsetDateTime.parse("1978-08-04T00:00:00+02:00").withNano(0).toString()).rate(9).build(),
      PersonDto.builder().perId(4).firstName("Tom").secondName("Cow").lastName("Boom")
          .birthday(OffsetDateTime.parse("1996-05-01T00:00:00+02:00").withNano(0).toString()).rate(10).build(),
      PersonDto.builder().perId(5).firstName("Pati").lastName("Anderson")
          .birthday(OffsetDateTime.parse("2000-07-20T00:00:00+02:00").withNano(0).toString()).rate(5).build() };

  private ActorFacade actorFacade;

  @Mock
  private Environment env;

  @Mock
  private ActorRepo actorRepo;

  @Mock
  private PersonRepo personRepo;

  @BeforeEach
  public void setup() {
    mockedActors[0].setPerId(1);

    mockedActors[1].setPerId(2);
    mockedActors[1].setSecondName("Lee");

    mockedActors[2].setPerId(3);

    mockedActors[3].setPerId(4);
    mockedActors[3].setSecondName("Cow");

    mockedActors[4].setPerId(5);

    MockitoAnnotations.initMocks(this);
    actorFacade = new ActorFacade(env, actorRepo, personRepo);
  }

  @Test
  public void executeUnknownCommandTest() {
    Cmd<Void, Void> command = new Cmd<Void, Void>() {
    };
    Assertions.assertThrows(UnsupportedOperationException.class, () -> actorFacade.execute(command));
  }

  @Test
  public void addActorTest() {
    PersonDto invalidActor = PersonDto.builder().build();
    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new AddActorCmd(invalidActor)));

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new AddActorCmd(invalidActor)));

    PersonDto validActor = PersonDto.builder().firstName("Jan").lastName("Kowalski")
        .birthday("1996-08-04T00:00:00+02:00").rate(10).build();
    Person person = new Person(validActor.getFirstName(), validActor.getLastName(),
        OffsetDateTime.parse(validActor.getBirthday()), validActor.getRate(), false);
    person.setPerId(1);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(person);

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Assertions.assertEquals(1, ((AddActorCmd) actorFacade.execute(new AddActorCmd(validActor))).getOutput());

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Assertions.assertEquals(1, ((AddActorCmd) actorFacade.execute(new AddActorCmd(validActor))).getOutput());
  }

  @Test
  public void deleteActorTest() {
    Person mockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10, false);
    Actor[] mockedRolesAssignemnt = new Actor[] { new Actor(mockedActor, null), new Actor(mockedActor, null),
        new Actor(mockedActor, null), new Actor(mockedActor, null) };

    mockedRolesAssignemnt[0].setActId(1);
    mockedRolesAssignemnt[1].setActId(2);
    mockedRolesAssignemnt[2].setActId(3);
    mockedRolesAssignemnt[3].setActId(4);

    mockedActor.setPerId(1);
    mockedActor.setActors(Arrays.asList(mockedRolesAssignemnt));

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(100, false)).thenReturn(null);
    Mockito.when(personRepo.save(mockedActor)).thenAnswer(new Answer<Person>() {

      @Override
      public Person answer(InvocationOnMock invocation) throws Throwable {
        mockedActor.setAct(false);

        return mockedActor;
      }

    });

    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new DeleteActorCmd(100)));
    Assertions.assertDoesNotThrow(() -> actorFacade.execute(new DeleteActorCmd(1)));
    Assertions.assertFalse(mockedActor.isAct());

    for (Actor roleAssignment : mockedActor.getActors()) {
      Assertions.assertFalse(roleAssignment.isAct());
    }

    Person invalidMockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10,
        false);
    Actor[] invalidMockedRolesAssignemnt = new Actor[] { new Actor(mockedActor, null), new Actor(mockedActor, null),
        new Actor(mockedActor, null), new Actor(mockedActor, null) };

    invalidMockedRolesAssignemnt[0].setActId(1);
    invalidMockedRolesAssignemnt[1].setActId(2);
    invalidMockedRolesAssignemnt[2].setActId(3);
    invalidMockedRolesAssignemnt[3].setActId(4);

    invalidMockedActor.setPerId(1);
    invalidMockedActor.setActors(Arrays.asList(invalidMockedRolesAssignemnt));

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(invalidMockedActor);
    Mockito.when(personRepo.findFirstByPerIdAndDirector(100, false)).thenReturn(null);
    Mockito.when(personRepo.save(invalidMockedActor)).thenReturn(invalidMockedActor);

    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new DeleteActorCmd(100)));
    Assertions.assertDoesNotThrow(() -> actorFacade.execute(new DeleteActorCmd(1)));
    Assertions.assertTrue(invalidMockedActor.isAct());

    for (Actor roleAssignment : invalidMockedActor.getActors()) {
      Assertions.assertTrue(roleAssignment.isAct());
    }
  }

  @Test
  public void editActorTest() {
    PersonDto invalidActor = PersonDto.builder().build();
    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new EditActorCmd(invalidActor)));

    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActors[0]);
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new EditActorCmd(invalidActor)));

    Person mockedActor = new Person("Tom", "Cruise", OffsetDateTime.parse("1962-07-03T00:00:00+02:00"), 10, false);
    mockedActor.setPerId(10);

    Person modifiedActor = new Person("Mot", "Esiurc", OffsetDateTime.parse("1999-07-03T00:00:00+02:00"), 1, false);
    modifiedActor.setPerId(10);
    modifiedActor.setSecondName("Test");

    PersonDto payload = PersonDto.builder().perId(10).firstName("Mot").secondName("Test").lastName("Esiurc")
        .birthday("1999-07-03T00:00+02:00").rate(1).act(true).build();

    Mockito.when(personRepo.findFirstByPerIdAndDirector(10, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(modifiedActor);

    EditActorCmd command = new EditActorCmd(payload);
    command = (EditActorCmd) actorFacade.execute(command);

    Assertions.assertEquals(payload.getPerId(), command.getOutput().getPerId());
    Assertions.assertEquals(payload.getFirstName(), command.getOutput().getFirstName());
    Assertions.assertEquals(payload.getSecondName(), command.getOutput().getSecondName());
    Assertions.assertEquals(payload.getLastName(), command.getOutput().getLastName());
    Assertions.assertEquals(payload.getBirthday(), command.getOutput().getBirthday());
    Assertions.assertEquals(payload.getRate(), command.getOutput().getRate());
    Assertions.assertEquals(payload.getAct(), command.getOutput().getAct());

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new EditActorCmd(invalidActor)));

    Mockito.when(personRepo.findFirstByPerIdAndDirector(1, false)).thenReturn(mockedActors[0]);
    Assertions.assertThrows(ValidationException.class, () -> actorFacade.execute(new EditActorCmd(invalidActor)));

    modifiedActor.setAct(false);

    payload.setAct(false);

    Mockito.when(personRepo.findFirstByPerIdAndDirector(10, false)).thenReturn(mockedActor);
    Mockito.when(personRepo.save(Mockito.any())).thenReturn(modifiedActor);

    command = new EditActorCmd(payload);
    command = (EditActorCmd) actorFacade.execute(command);

    Assertions.assertEquals(payload.getPerId(), command.getOutput().getPerId());
    Assertions.assertEquals(payload.getFirstName(), command.getOutput().getFirstName());
    Assertions.assertEquals(payload.getSecondName(), command.getOutput().getSecondName());
    Assertions.assertEquals(payload.getLastName(), command.getOutput().getLastName());
    Assertions.assertEquals(payload.getBirthday(), command.getOutput().getBirthday());
    Assertions.assertEquals(payload.getRate(), command.getOutput().getRate());
    Assertions.assertEquals(payload.getAct(), command.getOutput().getAct());
  }

  @Test
  public void getActorsTest() {
    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "valid" });
    ActorsFilter filter = new ActorsFilter(null, null, null, 0, 0);
    GetActorsCmd command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(0, command.getOutput().getContent().size());

    filter = new ActorsFilter(null, null, null, 1, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[0]);
        add(mockedActors[1]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(1, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }

    filter = new ActorsFilter(null, null, null, 2, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[2]);
        add(mockedActors[3]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(2, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i + 2];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }

    filter = new ActorsFilter(null, null, null, 3, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[4]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(3, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i + 4];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }

    Mockito.when(env.getActiveProfiles()).thenReturn(new String[] { "invalid" });
    filter = new ActorsFilter(null, null, null, 0, 0);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(0, command.getOutput().getContent().size());

    filter = new ActorsFilter(null, null, null, 1, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[0]);
        add(mockedActors[1]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(1, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }

    filter = new ActorsFilter(null, null, null, 2, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[2]);
        add(mockedActors[3]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(2, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i + 2];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }

    filter = new ActorsFilter(null, null, null, 3, 2);
    Mockito.when(personRepo.findByActorsFilter(Mockito.any())).thenReturn(new LinkedList<Person>() {
      {
        add(mockedActors[4]);
      }
    });
    Mockito.when(personRepo.countByActorsFilter(Mockito.any())).thenReturn(5l);
    command = new GetActorsCmd(filter);
    command = (GetActorsCmd) actorFacade.execute(command);
    Assertions.assertEquals(3, command.getOutput().getPage());
    Assertions.assertEquals(3, command.getOutput().getPages());
    Assertions.assertEquals(2, command.getOutput().getSize());
    Assertions.assertNotNull(command.getOutput().getContent());

    for (int i = 0; i < command.getOutput().getContent().size(); i++) {
      PersonDto actor = (PersonDto) command.getOutput().getContent().get(i);
      PersonDto mockedActor = mockedActorsDto[i + 4];
      Assertions.assertEquals(actor.getPerId(), mockedActor.getPerId());
      Assertions.assertEquals(actor.getFirstName(), mockedActor.getFirstName());
      Assertions.assertEquals(actor.getSecondName(), mockedActor.getSecondName());
      Assertions.assertEquals(actor.getLastName(), mockedActor.getLastName());
      Assertions.assertEquals(actor.getBirthday(), mockedActor.getBirthday());
      Assertions.assertEquals(actor.getRate(), mockedActor.getRate());
    }
  }

}
