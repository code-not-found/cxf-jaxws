package com.codenotfound.soap.http.cxf;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codenotfound.services.helloworld.Greeting;
import com.codenotfound.services.helloworld.HelloWorldPortType;
import com.codenotfound.services.helloworld.Person;

public class HelloWorldImplTest {

  private static String ENDPOINT_ADDRESS =
      "http://localhost:9080/codenotfound/services/helloworld";

  private static HelloWorldPortType helloWorldRequesterProxy;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createServerEndpoint();
    helloWorldRequesterProxy = createClientProxy();
  }

  @Test
  public void testSayHelloProxy() {
    Person person = new Person();
    person.setFirstName("Jane");
    person.setLastName("Doe");

    Greeting greeting = helloWorldRequesterProxy.sayHello(person);
    assertEquals("Hello Jane Doe!", greeting.getText());
  }

  private static void createServerEndpoint() {
    JaxWsServerFactoryBean jaxWsServerFactoryBean =
        new JaxWsServerFactoryBean();

    // create the loggingFeature
    LoggingFeature loggingFeature = new LoggingFeature();
    loggingFeature.setPrettyLogging(true);

    // add the loggingFeature to print the received/sent messages
    jaxWsServerFactoryBean.getFeatures().add(loggingFeature);

    HelloWorldImpl implementor = new HelloWorldImpl();
    jaxWsServerFactoryBean.setServiceBean(implementor);
    jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);

    jaxWsServerFactoryBean.create();
  }

  private static HelloWorldPortType createClientProxy() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();

    // create the loggingInInterceptor and loggingOutInterceptor
    LoggingInInterceptor loggingInInterceptor =
        new LoggingInInterceptor();
    loggingInInterceptor.setPrettyLogging(true);
    LoggingOutInterceptor loggingOutInterceptor =
        new LoggingOutInterceptor();
    loggingOutInterceptor.setPrettyLogging(true);

    // add loggingInterceptor to print the received/sent messages
    jaxWsProxyFactoryBean.getInInterceptors()
        .add(loggingInInterceptor);
    jaxWsProxyFactoryBean.getInFaultInterceptors()
        .add(loggingInInterceptor);
    jaxWsProxyFactoryBean.getOutInterceptors()
        .add(loggingOutInterceptor);
    jaxWsProxyFactoryBean.getOutFaultInterceptors()
        .add(loggingOutInterceptor);

    jaxWsProxyFactoryBean.setServiceClass(HelloWorldPortType.class);
    jaxWsProxyFactoryBean.setAddress(ENDPOINT_ADDRESS);

    return (HelloWorldPortType) jaxWsProxyFactoryBean.create();
  }
}
