package com.codenotfound.client;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codenotfound.client.HelloWorldClient;
import com.codenotfound.endpoint.HelloWorldImpl;
import com.codenotfound.services.helloworld.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = {"classpath:/META-INF/spring/context-requester.xml"})
public class HelloWorldClientTest {

  private static String ENDPOINT_ADDRESS =
      "http://localhost:9090/codenotfound/services/helloworld";

  @Autowired
  private HelloWorldClient helloWorldClientBean;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createServerEndpoint();
  }

  @Test
  public void testSayHello() {
    Person person = new Person();
    person.setFirstName("Jane");
    person.setLastName("Doe");

    assertEquals("Hello Jane Doe!",
        helloWorldClientBean.sayHello(person));
  }

  private static void createServerEndpoint() {
    // use a CXF JaxWsServerFactoryBean to create JAX-WS endpoints
    JaxWsServerFactoryBean jaxWsServerFactoryBean =
        new JaxWsServerFactoryBean();

    // set the HelloWorld implementation
    HelloWorldImpl implementor = new HelloWorldImpl();
    jaxWsServerFactoryBean.setServiceBean(implementor);

    // set the address at which the HelloWorld endpoint will be exposed
    jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);

    // create the server
    jaxWsServerFactoryBean.create();
  }
}
