package pl.tss.restbox.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * External person representation.
 *
 * @author TSS
 */
@Data
@JsonDeserialize(builder = PersonDto.PersonDtoBuilder.class)
public class PersonDto {

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

}
