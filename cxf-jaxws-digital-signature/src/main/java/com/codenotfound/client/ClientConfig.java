package com.codenotfound.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

  @Value("${client.ticketagent.address}")
  private String address;

  @Value("${client.ticketagent.keystore-alias}")
  private String keystoreAlias;

  @Bean(name = "ticketAgentProxyBean")
  public TicketAgent ticketAgent() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TicketAgent.class);
    jaxWsProxyFactoryBean.setAddress(address);

    // add the WSS4J OUT interceptor to sign the request message
    jaxWsProxyFactoryBean.getOutInterceptors().add(clientWssOut());
    // add the WSS4J IN interceptor to verify the signature on the response message
    jaxWsProxyFactoryBean.getInInterceptors().add(clientWssIn());

    // log the request and response messages
    jaxWsProxyFactoryBean.getInInterceptors()
        .add(loggingInInterceptor());
    jaxWsProxyFactoryBean.getOutInterceptors()
        .add(loggingOutInterceptor());

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }

  @Bean
  public Map<String, Object> clientOutProps() {
    Map<String, Object> clientOutProps = new HashMap<>();
    clientOutProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    clientOutProps.put(WSHandlerConstants.USER, keystoreAlias);
    clientOutProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
        ClientCallbackHandler.class.getName());
    clientOutProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "client_key.properties");

    return clientOutProps;
  }

  @Bean
  public WSS4JOutInterceptor clientWssOut() {
    WSS4JOutInterceptor clientWssOut = new WSS4JOutInterceptor();
    clientWssOut.setProperties(clientOutProps());

    return clientWssOut;
  }

  @Bean
  public Map<String, Object> clientInProps() {
    Map<String, Object> clientInProps = new HashMap<>();
    clientInProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    clientInProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "client_trust.properties");

    return clientInProps;
  }

  @Bean
  public WSS4JInInterceptor clientWssIn() {
    WSS4JInInterceptor clientWssIn = new WSS4JInInterceptor();
    clientWssIn.setProperties(clientInProps());

    return clientWssIn;
  }

  @Bean
  public LoggingInInterceptor loggingInInterceptor() {
    return new LoggingInInterceptor();
  }

  @Bean
  public LoggingOutInterceptor loggingOutInterceptor() {
    return new LoggingOutInterceptor();
  }
}
