package pl.tss.restbox.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.tss.restbox.core.domain.filter.Sortable;

/**
 * External person representation.
 *
 * @author TSS
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = PersonDto.PersonDtoBuilder.class)
public class PersonDto extends PageableDto {

  private Integer perId;
  private String firstName;
  private String secondName;
  private String lastName;
  private String birthday;
  private Integer age;
  private Integer rate;
  private Boolean act;

  @Builder
  public PersonDto(Integer perId, String firstName, String secondName, String lastName, String birthday, Integer age,
      Integer rate, Boolean act) {
    this.perId = perId;
    this.firstName = firstName;
    this.secondName = secondName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.age = age;
    this.rate = rate;
    this.act = act;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class PersonDtoBuilder {
  }

  /**
   * Definition of sorting for person DTO.
   */
  public enum SortColumn implements Sortable {

    PER_ID("perid", "actor.perId"), FIRST_NAME("firstname", "actor.firstName"),
    SECOND_NAME("secondname", "actor.secondName"), LAST_NAME("lastname", "actor.lastName"),
    BIRTHDAY("birthday", "actor.birthday"), AGE("age", "actor.age"), RATE("rate", "actor.rate"),
    ACT("act", "actor.act");

    private final String field;
    private final String query;

    SortColumn(String field, String query) {
      this.field = field;
      this.query = query;
    }

    @Override
    public Sortable[] getColumns() {
      return SortColumn.values();
    }

    @Override
    public String getField() {
      return field;
    }

    @Override
    public String getQuery() {
      return query;
    }

  }

}
