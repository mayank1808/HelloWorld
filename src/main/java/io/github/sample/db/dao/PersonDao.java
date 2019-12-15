package io.github.sample.db.dao;

import io.github.sample.db.entity.Person;
import org.hibernate.SessionFactory;

public class PersonDao extends EntityBaseDao<Person> {

  public PersonDao(SessionFactory sessionFactory) {
    super("id", sessionFactory, Person.class);
  }

}
