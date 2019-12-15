package io.github.sample;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
import com.google.inject.Stage;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.sample.db.dao.DaoModule;
import io.github.sample.db.entity.Person;
import io.github.sample.health.TemplateHealthCheck;
import io.github.sample.resources.HelloWorldResource;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {


  private GuiceBundle<HelloWorldConfiguration> guiceBundle;

  public static void main(final String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  @Override
  public String getName() {
    return "HelloWorld";
  }

  private final HibernateBundle<HelloWorldConfiguration> hibernate = new HibernateBundle<HelloWorldConfiguration>(
      Person.class) {

    @Override
    protected String name() {
      return "HELLO-WORLD";
    }


    @Override
    public PooledDataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
      return configuration.getDatabase();
    }
  };

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

    environment.jersey().register(ClassPath.ResourceInfo.class);

    final TemplateHealthCheck healthCheck =
        new TemplateHealthCheck(configuration.getTemplate());
    environment.healthChecks().register("template", healthCheck);
  }

}
