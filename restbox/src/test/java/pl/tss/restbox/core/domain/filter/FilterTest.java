package pl.tss.restbox.core.domain.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Overall result set filter unit tests.
 *
 * @author TSS
 */
public class FilterTest {

  @Test
  public void addSortTest() {
    int expectedSize = 2;
    String sortQuery = "name,asc;id,desc";
    Filter filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());
    Assertions.assertTrue(filter.getSort().containsKey(SortColumn.TEST_ID));
    Assertions.assertTrue(filter.getSort().containsKey(SortColumn.TEST_NAME));

    Sortable.Direction expectedDirection = Sortable.Direction.DESCENDING;
    Assertions.assertEquals(expectedDirection, filter.getSort().get(SortColumn.TEST_ID));

    expectedDirection = Sortable.Direction.ASCENDING;
    Assertions.assertEquals(expectedDirection, filter.getSort().get(SortColumn.TEST_NAME));

    expectedSize = 0;
    sortQuery = "";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());

    expectedSize = 0;
    sortQuery = "nametest,asc;idtest,desc";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());

    expectedSize = 0;
    sortQuery = "name,asc id,desc";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());

    expectedSize = 0;
    sortQuery = "name asc;id desc";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());

    expectedSize = 0;
    sortQuery = "name,";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());

    expectedSize = 0;
    sortQuery = "name,;id,";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expectedSize, filter.getSort().size());
  }

  @Test
  public void getSortQueryTest() {
    String expected = " order by test.name asc, test.id desc";
    String sortQuery = "name,asc;id,desc";
    Filter filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expected, filter.getSortQuery());

    expected = "";
    sortQuery = "";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expected, filter.getSortQuery());

    expected = "";
    sortQuery = "nametest,asc;idtest,desc";
    filter = new TestFilter();
    filter.addSort(sortQuery, SortColumn.values());
    Assertions.assertEquals(expected, filter.getSortQuery());
  }

  private class TestFilter extends Filter {
  }

  private enum SortColumn implements Sortable {

    TEST_ID("id", "test.id"), TEST_NAME("name", "test.name");

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
