package io.github.sample;

import com.google.inject.Stage;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.sample.db.dao.DaoModule;
import io.github.sample.db.entity.Person;
import io.github.sample.health.TemplateHealthCheck;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {


  private GuiceBundle<HelloWorldConfiguration> guiceBundle;

  private final HibernateBundle<HelloWorldConfiguration> hibernate = new HibernateBundle<HelloWorldConfiguration>(
      Person.class) {

    @Override
    protected String name() {
      return "HelloWorld";
    }

    @Override
    public PooledDataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
      return configuration.getDatabase();
    }
  };
  public static void main(final String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  @Override
  public String getName() {
    return "HelloWorld";
  }

  @Override
  public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
    bootstrap.addBundle(hibernate);

    guiceBundle = GuiceBundle.<HelloWorldConfiguration>builder()
        .enableAutoConfig(getClass().getPackage().getName())
        .modules(
            new DaoModule(hibernate)
        )
        .build(Stage.DEVELOPMENT);
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(final HelloWorldConfiguration configuration,
      final Environment environment) {

    final TemplateHealthCheck healthCheck =
        new TemplateHealthCheck(configuration.getTemplate());
    environment.healthChecks().register("template", healthCheck);
  }

}
