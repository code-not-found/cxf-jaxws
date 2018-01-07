package com.codenotfound.soap.http.cxf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codenotfound.services.helloworld.Greeting;
import com.codenotfound.services.helloworld.HelloWorldPortType;
import com.codenotfound.services.helloworld.Person;

@Component
public class HelloWorldClient {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(HelloWorldClient.class);

  @Autowired
  private HelloWorldPortType helloWorldRequesterBean;

  public String sayHello(Person person) {
    Greeting greeting = helloWorldRequesterBean.sayHello(person);

    String result = greeting.getText();
    LOGGER.info("result={}", result);
    return result;
  }
}
