package io.github.sample.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import io.github.sample.HelloWorldConfiguration;
import io.github.sample.api.Saying;
import io.github.sample.db.dao.PersonDao;
import io.github.sample.db.entity.Person;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class HelloWorldResource {

  private final String template;
  private final String defaultName;
  private final PersonDao personDao;

  @Inject
  public HelloWorldResource(HelloWorldConfiguration configuration, PersonDao personDao) {
    log.info("Injected");
    this.template = configuration.getTemplate();
    this.defaultName = configuration.getDefaultName();
    this.personDao = personDao;
    if(personDao == null){
      log.error("LOL");
    }else{
      log.info("FINE");
    }
  }

  @GET
  @Timed
  public Saying sayHello(@QueryParam("name") Optional<String> name) {
    final String value = String.format(template, name.orElse(defaultName));

    if(personDao == null){
      log.error("Person Dao null");
    }
    final Person savedEntity = personDao.save(
        Person.builder()
            .name(name.orElse(defaultName))
            .build()
    );
    return new Saying(savedEntity.getId(), value);
  }


}
