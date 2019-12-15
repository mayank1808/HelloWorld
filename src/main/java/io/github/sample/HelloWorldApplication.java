package io.github.sample;

import com.google.common.reflect.ClassPath;
import com.google.inject.Stage;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.qtrouper.TrouperBundle;
import io.github.qtrouper.core.rabbit.RabbitConfiguration;
import io.github.sample.health.TemplateHealthCheck;
import io.github.sample.resources.HelloWorldResource;
import java.util.Arrays;
import java.util.List;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

  private final List<String> PACKAGE_NAME_LIST = Arrays.asList(
      "io.github.sample"
  );


  public static void main(final String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  @Override
  public String getName() {
    return "HelloWorld";
  }

  @Override
  public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {

    TrouperBundle<HelloWorldConfiguration> trouperBundle = new TrouperBundle<HelloWorldConfiguration>() {

      @Override
      public RabbitConfiguration getRabbitConfiguration(HelloWorldConfiguration configuration) {
        return configuration.getRabbitConfiguration();
      }
    };

    bootstrap.addBundle(trouperBundle);

    GuiceBundle<HelloWorldConfiguration> guiceBundle = GuiceBundle.<HelloWorldConfiguration>builder()
        .enableAutoConfig(getClass().getPackage().getName())
        .modules(
            new HelloWorldModule(trouperBundle)
        )
        .build(Stage.DEVELOPMENT);

    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(final HelloWorldConfiguration configuration,
      final Environment environment) {

    environment.jersey().register(ClassPath.ResourceInfo.class);

    final TemplateHealthCheck healthCheck =
        new TemplateHealthCheck(configuration.getTemplate());
    environment.healthChecks().register("template", healthCheck);

  }

}
