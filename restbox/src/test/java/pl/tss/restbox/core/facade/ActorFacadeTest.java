package pl.tss.restbox.core.facade;

import java.time.OffsetDateTime;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.actor.AddActorCmd;
import pl.tss.restbox.core.domain.command.actor.GetActorsCmd;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.domain.exception.ValidationException;
import pl.tss.restbox.core.domain.filter.ActorsFilter;
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
    actorFacade = new ActorFacade(env, personRepo);
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
