package com.codenotfound.client;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

  @Value("${client.ticketagent.address}")
  private String address;

  @Value("${client.ticketagent.user-name}")
  private String userName;

  @Value("${client.ticketagent.password}")
  private String password;

  @Bean(name = "ticketAgentProxy")
  public TicketAgent ticketAgentProxy() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TicketAgent.class);
    jaxWsProxyFactoryBean.setAddress(address);

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }

  @Bean
  public Client ticketAgentClientProxy() {
    return ClientProxy.getClient(ticketAgentProxy());
  }

  @Bean
  public HTTPConduit ticketAgentConduit() {
    HTTPConduit httpConduit =
        (HTTPConduit) ticketAgentClientProxy().getConduit();
    httpConduit.setAuthorization(basicAuthorization());

    return httpConduit;
  }

  @Bean
  public AuthorizationPolicy basicAuthorization() {
    AuthorizationPolicy authorizationPolicy =
        new AuthorizationPolicy();
    authorizationPolicy.setUserName(userName);
    authorizationPolicy.setPassword(password);
    authorizationPolicy.setAuthorizationType("Basic");

    return authorizationPolicy;
  }
}
