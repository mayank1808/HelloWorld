package io.github.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HelloWorldConfiguration extends Configuration {

  @NotEmpty
  private String template;

  @NotEmpty
  private String defaultName = "Stranger";

  @JsonProperty
  @Valid
  private RabbitConfiguration rabbitConfiguration = new RabbitConfiguration();

  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();
}
