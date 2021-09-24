package pl.tss.restbox.core.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Facade unit tests.
 *
 * @author TSS
 */
public class FacadeTest {

  private Facade facade;

  @BeforeEach
  public void setup() {
    facade = new Facade() {

      @Override
      public Cmd<?, ?> execute(Cmd<?, ?> command) {
        throw new UnsupportedOperationException();
      }

    };
  }

  @Test
  public void isValidProfileTest() {
    String[] firstSet = new String[] { "dev", "test", "invalid" };
    Assertions.assertFalse(facade.isValidProfile(firstSet));

    String[] secondSet = new String[] { "dev", "test", "valid" };
    Assertions.assertTrue(facade.isValidProfile(secondSet));
  }

}
