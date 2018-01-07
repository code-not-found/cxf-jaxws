package com.codenotfound.soap.http.cxf;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codenotfound.services.helloworld.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = {"classpath:/META-INF/spring/context-requester.xml"})
public class HelloWorldImplTest {

  private static String ENDPOINT_ADDRESS =
      "http://localhost:9090/cnf/services/helloworld";

  @Autowired
  private HelloWorldClient helloWorldClient;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    // start the HelloWorld service using jaxWsServerFactoryBean
    JaxWsServerFactoryBean jaxWsServerFactoryBean =
        new JaxWsServerFactoryBean();

    // adding loggingFeature to print the received/sent messages
    LoggingFeature loggingFeature = new LoggingFeature();
    loggingFeature.setPrettyLogging(true);

    jaxWsServerFactoryBean.getFeatures().add(loggingFeature);

    // setting the implementation
    HelloWorldImpl implementor = new HelloWorldImpl();
    jaxWsServerFactoryBean.setServiceBean(implementor);
    // setting the endpoint
    jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);
    jaxWsServerFactoryBean.create();
  }

  @Test
  public void testSayHello() {
    Person person = new Person();
    person.setFirstName("John");
    person.setLastName("Doe");

    assertEquals("Hello John Doe!",
        helloWorldClient.sayHello(person));
  }
}
