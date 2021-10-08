package pl.tss.restbox.core.domain.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Sortable data structure unit tests.
 *
 * @author TSS
 */
public class SortableTest {

  @Test
  public void sortableDirectionTest() {
    int expectedSize = 2;
    Assertions.assertEquals(expectedSize, Sortable.Direction.values().length);

    String expectedAsc = "asc";
    Assertions.assertEquals(expectedAsc, Sortable.Direction.ASCENDING.getValue());

    String expectedDesc = "desc";
    Assertions.assertEquals(expectedDesc, Sortable.Direction.DESCENDING.getValue());
  }

  @Test
  public void sortableDirectionFromValueTest() {
    String asc = "asc";
    String desc = "desc";

    Sortable.Direction expectedDirection = Sortable.Direction.ASCENDING;
    Assertions.assertEquals(expectedDirection, Sortable.Direction.fromValue(null));
    Assertions.assertEquals(expectedDirection, Sortable.Direction.fromValue(asc));

    expectedDirection = Sortable.Direction.DESCENDING;
    Assertions.assertEquals(expectedDirection, Sortable.Direction.fromValue(desc));
  }

}
