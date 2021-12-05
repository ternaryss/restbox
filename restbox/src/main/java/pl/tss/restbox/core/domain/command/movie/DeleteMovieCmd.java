package pl.tss.restbox.core.domain.command.movie;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Delete movie command.
 *
 * @author TSS
 */
public class DeleteMovieCmd extends Cmd<Integer, Void> {

  public DeleteMovieCmd(Integer movId) {
    super(movId);
  }

}
