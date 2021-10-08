package pl.tss.restbox.core.domain.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for external person data structure.
 *
 * @author TSS
 */
public class PersonDtoTest {

  @Test
  public void personDtoSortColumnTest() {
    int expectedSize = 7;
    Assertions.assertEquals(expectedSize, PersonDto.SortColumn.values().length);

    String expectedPerId = "perid";
    String expectedPerIdQuery = "actor.perId";
    Assertions.assertEquals(expectedPerId, PersonDto.SortColumn.PER_ID.getField());
    Assertions.assertEquals(expectedPerIdQuery, PersonDto.SortColumn.PER_ID.getQuery());

    String expectedFirstName = "firstname";
    String expectedFirstNameQuery = "actor.firstName";
    Assertions.assertEquals(expectedFirstName, PersonDto.SortColumn.FIRST_NAME.getField());
    Assertions.assertEquals(expectedFirstNameQuery, PersonDto.SortColumn.FIRST_NAME.getQuery());

    String expectedSecondName = "secondname";
    String expectedSecondNameQuery = "actor.secondName";
    Assertions.assertEquals(expectedSecondName, PersonDto.SortColumn.SECOND_NAME.getField());
    Assertions.assertEquals(expectedSecondNameQuery, PersonDto.SortColumn.SECOND_NAME.getQuery());

    String expectedLastName = "lastname";
    String expectedLastNameQuery = "actor.lastName";
    Assertions.assertEquals(expectedLastName, PersonDto.SortColumn.LAST_NAME.getField());
    Assertions.assertEquals(expectedLastNameQuery, PersonDto.SortColumn.LAST_NAME.getQuery());

    String expectedBirthday = "birthday";
    String expectedBirthdayQuery = "actor.birthday";
    Assertions.assertEquals(expectedBirthday, PersonDto.SortColumn.BIRTHDAY.getField());
    Assertions.assertEquals(expectedBirthdayQuery, PersonDto.SortColumn.BIRTHDAY.getQuery());

    String expectedRate = "rate";
    String expectedRateQuery = "actor.rate";
    Assertions.assertEquals(expectedRate, PersonDto.SortColumn.RATE.getField());
    Assertions.assertEquals(expectedRateQuery, PersonDto.SortColumn.RATE.getQuery());

    String expectedAct = "act";
    String expectedActQuery = "actor.act";
    Assertions.assertEquals(expectedAct, PersonDto.SortColumn.ACT.getField());
    Assertions.assertEquals(expectedActQuery, PersonDto.SortColumn.ACT.getQuery());
  }

}
