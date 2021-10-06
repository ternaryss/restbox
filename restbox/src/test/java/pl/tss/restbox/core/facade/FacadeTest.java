package pl.tss.restbox.core.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import pl.tss.restbox.core.domain.command.Cmd;

/**
 * Facade unit tests.
 *
 * @author TSS
 */
public class FacadeTest {

  private Facade facade;

  @Mock
  private Environment env;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    facade = new Facade(env) {

      @Override
      public Cmd<?, ?> execute(Cmd<?, ?> command) {
        throw new UnsupportedOperationException();
      }

    };
  }

  @Test
  public void isValidProfileTest() {
    String[] firstSet = new String[] { "dev", "test", "invalid" };
    Mockito.when(env.getActiveProfiles()).thenReturn(firstSet);
    Assertions.assertFalse(facade.isValidProfile());

    String[] secondSet = new String[] { "dev", "test", "valid" };
    Mockito.when(env.getActiveProfiles()).thenReturn(secondSet);
    Assertions.assertTrue(facade.isValidProfile());
  }

}
