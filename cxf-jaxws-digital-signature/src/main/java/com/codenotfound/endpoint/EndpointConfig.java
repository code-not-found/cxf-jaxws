package com.codenotfound.endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {

  @Value("${server.ticketagent.keystore-alias}")
  private String keystoreAlias;

  @Autowired
  private Bus bus;

  @Bean
  public Endpoint endpoint() {
    EndpointImpl endpoint =
        new EndpointImpl(bus, new TicketAgentImpl());
    endpoint.publish("/ticketagent");

    // add the WSS4J IN interceptor to verify the signature on the request message
    endpoint.getInInterceptors().add(serverWssIn());
    // add the WSS4J OUT interceptor to sign the response message
    endpoint.getOutInterceptors().add(serverWssOut());

    return endpoint;
  }

  @Bean
  public Map<String, Object> serverInProps() {
    Map<String, Object> serverInProps = new HashMap<>();
    serverInProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    serverInProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "server_trust.properties");

    return serverInProps;
  }

  @Bean
  public WSS4JInInterceptor serverWssIn() {
    WSS4JInInterceptor serverWssIn = new WSS4JInInterceptor();
    serverWssIn.setProperties(serverInProps());

    return serverWssIn;
  }

  @Bean
  public Map<String, Object> serverOutProps() {
    Map<String, Object> serverOutProps = new HashMap<>();
    serverOutProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    serverOutProps.put(WSHandlerConstants.USER, keystoreAlias);
    serverOutProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
        ServerCallbackHandler.class.getName());
    serverOutProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "server_key.properties");

    return serverOutProps;
  }

  @Bean
  public WSS4JOutInterceptor serverWssOut() {
    WSS4JOutInterceptor serverWssOut = new WSS4JOutInterceptor();
    serverWssOut.setProperties(serverOutProps());

    return serverWssOut;
  }
}
