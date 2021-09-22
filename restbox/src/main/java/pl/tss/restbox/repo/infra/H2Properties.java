package pl.tss.restbox.repo.infra;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Database migration configuration properties.
 *
 * @author TSS
 */
@Component
@ConfigurationProperties(prefix = "spring.h2")
class H2Properties {

  @Getter
  private final String driver = "org.h2.Driver";

  @Setter
  private String file;

  @Setter
  @Getter
  private String username;

  @Setter
  @Getter
  private String password;

  @Setter
  @Getter
  private List<String> migrations;

  public String getUrl() {
    return "jdbc:h2:file:" + System.getProperty("user.dir") + file;
  }

}
