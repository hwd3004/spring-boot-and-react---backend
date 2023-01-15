package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  String[] ORIGINS = { "http://localhost:3000" };

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
      .addMapping("/**")
      .allowedOrigins(ORIGINS)
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true)
      .maxAge(3600);
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer
      .setUseTrailingSlashMatch(true)
      .setUseRegisteredSuffixPatternMatch(true);
    configurer.addPathPrefix("api", c -> true);
  }
}
