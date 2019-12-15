package io.github.sample;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.sample.health.TemplateHealthCheck;
import io.github.sample.resources.HelloWorldResource;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

  public static void main(final String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  @Override
  public String getName() {
    return "HelloWorld";
  }

  @Override
  public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
    // TODO: application initialization
  }

  @Override
  public void run(final HelloWorldConfiguration configuration,
      final Environment environment) {
    final HelloWorldResource resource = new HelloWorldResource(
        configuration.getTemplate(),
        configuration.getDefaultName()
    );
    environment.jersey().register(resource);

    final TemplateHealthCheck healthCheck =
        new TemplateHealthCheck(configuration.getTemplate());
    environment.healthChecks().register("template", healthCheck);
  }

}
