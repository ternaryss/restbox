package pl.tss.restbox.rest.infra;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * REST API configuration.
 *
 * @author TSS
 */
@Slf4j
@Configuration
class RestApiConfig {

  private final String lang;

  public RestApiConfig(@Value("${rest-api.lang}") String lang) {
    this.lang = lang;
  }

  @Bean
  public ResourceBundle msgBundle() {
    log.debug("Initializing messages bundle");
    ResourceBundle bundle = ResourceBundle.getBundle("msg/" + lang);
    log.debug("Messages bundle initialized [bundle = {}]", bundle.getBaseBundleName());

    return bundle;
  }

}
