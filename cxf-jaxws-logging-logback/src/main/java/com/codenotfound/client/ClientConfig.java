package com.codenotfound.client;

import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

  @Value("${client.ticketagent.address}")
  private String address;

  @Bean(name = "ticketAgentProxy")
  public TicketAgent ticketAgentProxy() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TicketAgent.class);
    jaxWsProxyFactoryBean.setAddress(address);

    // add an interceptor to log the outgoing request messages
    jaxWsProxyFactoryBean.getOutInterceptors()
        .add(loggingOutInterceptor());
    // add an interceptor to log the incoming response messages
    jaxWsProxyFactoryBean.getInInterceptors()
        .add(loggingInInterceptor());
    // add an interceptor to log the incoming fault messages
    jaxWsProxyFactoryBean.getInFaultInterceptors()
        .add(loggingInInterceptor());

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }

  @Bean
  public LoggingOutInterceptor loggingOutInterceptor() {
    return new LoggingOutInterceptor();
  }

  @Bean
  public LoggingInInterceptor loggingInInterceptor() {
    return new LoggingInInterceptor();
  }
}
