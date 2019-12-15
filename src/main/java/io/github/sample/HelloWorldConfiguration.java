package io.github.sample;

import io.dropwizard.Configuration;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class HelloWorldConfiguration extends Configuration {

  @NotEmpty
  private String template;

  @NotEmpty
  private String defaultName = "Stranger";
}
