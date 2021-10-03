package pl.tss.restbox.core.domain.command.actor;

import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.dto.PageDto;
import pl.tss.restbox.core.domain.filter.ActorsFilter;

/**
 * Get actors command.
 *
 * @author TSS
 */
public class GetActorsCmd extends Cmd<ActorsFilter, PageDto> {

  public GetActorsCmd(ActorsFilter filter) {
    super(filter);
  }

}
