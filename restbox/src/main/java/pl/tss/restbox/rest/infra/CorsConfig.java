package pl.tss.restbox.rest.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * CORS configuration.
 *
 * @author TSS
 */
@Slf4j
@EnableWebMvc
@Configuration
class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.debug("Disabling CORS policy in default registry [registry = {}]", registry);
    registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
    log.debug("CORS policy disabled in default registry [registry = {}]", registry);
  }

}
