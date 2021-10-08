package pl.tss.restbox.core.domain.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.tss.restbox.core.domain.dto.PageDto;

/**
 * Unit tests for pagination details.
 *
 * @author TSS
 */
public class PaginationTest {

  @Test
  public void countPagesTest() {
    int expectedPage = 1;
    int expectedLimit = 20;
    int expectedOffset = 0;
    Pagination pagination = new Pagination();
    PageDto page = pagination.generatePage(0, null);
    Assertions.assertEquals(expectedLimit, pagination.getLimit());
    Assertions.assertEquals(expectedOffset, pagination.getOffset());
    Assertions.assertNotNull(page);
    Assertions.assertEquals(expectedPage, page.getPage());
    Assertions.assertEquals(expectedLimit, page.getSize());

    int expectedPages = 4;
    pagination = new Pagination(1, 5);
    page = pagination.generatePage(20, null);
    Assertions.assertEquals(expectedPages, page.getPages());

    expectedPages = 4;
    pagination = new Pagination(1, 5);
    page = pagination.generatePage(18, null);
    Assertions.assertEquals(expectedPages, page.getPages());

    expectedPages = 0;
    pagination = new Pagination();
    page = pagination.generatePage(-100, null);
    Assertions.assertEquals(expectedPages, page.getPages());

    expectedPages = 0;
    pagination = new Pagination(1, 0);
    page = pagination.generatePage(100, null);
    Assertions.assertEquals(expectedPages, page.getPages());
  }

}
