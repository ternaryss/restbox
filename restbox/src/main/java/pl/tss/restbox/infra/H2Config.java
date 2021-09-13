package pl.tss.restbox.infra;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * H2 database configuration.
 *
 * @author TSS
 */
@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
class H2Config {

  @Value("${spring.h2.driver}")
  private String driver;

  @Value("${spring.h2.file}")
  private String file;

  @Value("${spring.h2.username}")
  private String username;

  @Value("${spring.h2.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    log.debug("Configuring H2");
    DataSourceBuilder<?> builder = DataSourceBuilder.create();
    builder.driverClassName(driver);
    builder.url("jdbc:h2:file:" + file);
    builder.username(username);
    builder.password(password);
    log.debug("H2 configured [builder = {}]", builder);

    return builder.build();
  }

}
