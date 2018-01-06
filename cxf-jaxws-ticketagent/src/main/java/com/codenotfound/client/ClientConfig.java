package com.codenotfound.client;

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

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }
}
