package pl.tss.restbox.core.domain.filter;

import lombok.Getter;

/**
 * Definition of sortable data structure.
 *
 * @author TSS
 */
public interface Sortable {

  String ORDER_BY = " order by ";

  Sortable[] getColumns();

  String getField();

  String getQuery();

  /**
   * Sortable direction.
   */
  public static enum Direction {

    ASCENDING("asc"), DESCENDING("desc");

    @Getter
    private final String value;

    Direction(String value) {
      this.value = value;
    }

  }

}
