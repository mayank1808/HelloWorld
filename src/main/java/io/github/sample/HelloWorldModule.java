package io.github.sample;

import com.google.inject.AbstractModule;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HelloWorldModule extends AbstractModule {

  @Override
  protected void configure() {
    //Bind classed here etc.
  }
}
