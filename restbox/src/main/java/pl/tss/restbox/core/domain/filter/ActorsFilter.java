package pl.tss.restbox.core.domain.filter;

import lombok.Getter;

/**
 * Actors filter.
 *
 * @author TSS
 */
public class ActorsFilter extends Filter {

  @Getter
  private final String firstName;

  @Getter
  private final String lastName;

  @Getter
  private final Integer rate;

  public ActorsFilter(String firstName, String lastName, Integer rate) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.rate = rate;
  }

  public ActorsFilter(String firstName, String lastName, Integer rate, Integer page, Integer pageSize) {
    super(page, pageSize);
    this.firstName = firstName;
    this.lastName = lastName;
    this.rate = rate;
  }

}
