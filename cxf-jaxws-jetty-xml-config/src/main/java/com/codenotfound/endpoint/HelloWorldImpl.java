package com.codenotfound.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenotfound.services.helloworld.Greeting;
import com.codenotfound.services.helloworld.HelloWorldPortType;
import com.codenotfound.services.helloworld.ObjectFactory;
import com.codenotfound.services.helloworld.Person;

public class HelloWorldImpl implements HelloWorldPortType {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(HelloWorldImpl.class);

  @Override
  public Greeting sayHello(Person person) {

    String firstName = person.getFirstName();
    LOGGER.info("firstName={}", firstName);
    String lasttName = person.getLastName();
    LOGGER.info("lastName={}", lasttName);

    ObjectFactory factory = new ObjectFactory();
    Greeting response = factory.createGreeting();

    String greeting = "Hello " + firstName + " " + lasttName + "!";
    LOGGER.info("greeting={}", greeting);

    response.setText(greeting);
    return response;
  }
}
