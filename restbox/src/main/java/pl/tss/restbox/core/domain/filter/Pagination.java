package pl.tss.restbox.core.domain.filter;

import java.util.List;

import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.dto.PageableDto;

/**
 * Result set pagination details.
 *
 * @author TSS
 */
public class Pagination {

  private final static int DEFAULT_PAGE = 1;
  private final static int DEFAULT_PAGE_SIZE = 20;

  private final int page;
  private final int size;

  public Pagination() {
    this.page = DEFAULT_PAGE;
    this.size = DEFAULT_PAGE_SIZE;
  }

  public Pagination(int page, int size) {
    this.page = page;
    this.size = size;
  }

  private int countPages(int resultSetSize) {
    int pages = resultSetSize / size;

    if (resultSetSize % size != 0) {
      pages = pages + 1;
    }

    return pages;
  }

  public PageDto generatePage(int resultSetSize, List<? extends PageableDto> content) {
    return PageDto.builder().page(page).size(size).pages(countPages(resultSetSize)).content(content).build();
  }

  public int getLimit() {
    return size;
  }

  public int getOffset() {
    return (page - 1) * size;
  }

}
