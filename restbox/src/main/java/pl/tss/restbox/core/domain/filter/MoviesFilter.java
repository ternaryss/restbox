package pl.tss.restbox.core.domain.filter;

import lombok.Getter;

/**
 * Movies filter.
 *
 * @author TSS
 */
public class MoviesFilter extends Filter {

  @Getter
  private final String title;

  @Getter
  private final String genere;

  @Getter
  private final String country;

  @Getter
  private final Integer rate;

  public MoviesFilter() {
    super();
    this.title = null;
    this.genere = null;
    this.country = null;
    this.rate = null;
  }

  public MoviesFilter(String title, String genere, String country, Integer rate) {
    super();
    this.title = title;
    this.genere = genere;
    this.country = country;
    this.rate = rate;
  }

  public MoviesFilter(String title, String genere, String country, Integer rate, Integer page, Integer pageSize) {
    super(page, pageSize);
    this.title = title;
    this.genere = genere;
    this.country = country;
    this.rate = rate;
  }

}
