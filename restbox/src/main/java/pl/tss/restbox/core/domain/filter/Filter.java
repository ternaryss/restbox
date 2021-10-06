package pl.tss.restbox.core.domain.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Overall result set filter definition.
 *
 * @author TSS
 */
public abstract class Filter {

  @Getter
  private final Map<Sortable, Sortable.Direction> sort;

  @Getter
  private final Pagination pagination;

  public Filter() {
    this.pagination = new Pagination();
    this.sort = new LinkedHashMap<>();
  }

  public Filter(Integer page, Integer pageSize) {
    this.pagination = new Pagination(page, pageSize);
    this.sort = new LinkedHashMap<>();
  }

  public void addSort(Sortable column, Sortable.Direction direction) {
    sort.put(column, direction);
  }

  public String getSortQuery() {
    StringBuilder builder = new StringBuilder();
    String query = "";

    if (!sort.isEmpty()) {
      builder.append(Sortable.ORDER_BY);

      for (Map.Entry<Sortable, Sortable.Direction> entry : sort.entrySet()) {
        String queryColumn = entry.getKey().getQuery();
        String rawDirection = entry.getValue().getValue();

        builder.append(queryColumn).append(" ").append(rawDirection).append(", ");
      }

      query = builder.substring(0, builder.toString().length() - 2);
    }

    return query;
  }

}
