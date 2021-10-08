package pl.tss.restbox.core.domain.filter;

import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.dto.PageableDto;

/**
 * Result set pagination details.
 *
 * @author TSS
 */
@Slf4j
public class Pagination {

  private final static int DEFAULT_PAGE = 1;
  private final static int DEFAULT_PAGE_SIZE = 20;

  @Getter
  private final int page;

  @Getter
  private final int size;

  public Pagination() {
    this.page = DEFAULT_PAGE;
    this.size = DEFAULT_PAGE_SIZE;
  }

  public Pagination(Integer page, Integer size) {
    this.page = page != null ? page : DEFAULT_PAGE;
    this.size = size != null ? size : DEFAULT_PAGE_SIZE;
  }

  private int countPages(long resultSetSize) {
    long pages = 0;
    log.debug("Counting pages [resultSetSize = {}, pageSize = {}]", resultSetSize, size);

    if (size != 0) {
      pages = resultSetSize / Long.valueOf(size);

      if (resultSetSize % size != 0) {
        pages = pages + 1;
      }
    }

    log.debug("Pages count [pages = {}]", pages);

    return (int) pages;
  }

  public PageDto generatePage(long resultSetSize, List<? extends PageableDto> content) {
    return PageDto.builder().page(page).size(size).pages(countPages(resultSetSize)).content(content).build();
  }

  public int getLimit() {
    return size;
  }

  public int getOffset() {
    return (page - 1) * size;
  }

}
