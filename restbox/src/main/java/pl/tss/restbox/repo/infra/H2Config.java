package pl.tss.restbox.repo.infra;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import lombok.extern.slf4j.Slf4j;

/**
 * H2 database configuration.
 *
 * @author TSS
 */
@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableJpaRepositories(considerNestedRepositories = true, basePackages = { "pl.tss.restbox.repo.db" })
class H2Config {

  private final H2Properties properties;

  public H2Config(H2Properties properties) {
    this.properties = properties;
  }

  @Bean
  public DataSource dataSource() {
    log.debug("Configuring H2");
    DataSourceBuilder<?> builder = DataSourceBuilder.create();
    builder.driverClassName(properties.getDriver());
    builder.url(properties.getUrl());
    builder.username(properties.getUsername());
    builder.password(properties.getPassword());
    log.debug("H2 configured [builder = {}]", builder.getClass().getSimpleName());

    return builder.build();
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    log.debug("Applaying migrations");
    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    List<String> migrations = properties.getMigrations();

    if (migrations != null) {
      for (String migration : migrations) {
        log.debug("Migration applied [migration = {}]", migration);
        populator.addScript(new ClassPathResource(migration));
      }
    }

    initializer.setDatabasePopulator(populator);
    log.debug("All migrations applied [migrationsSize = {}, initializer = {}]",
        migrations != null ? migrations.size() : null, initializer.getClass().getSimpleName());

    return initializer;
  }

}
