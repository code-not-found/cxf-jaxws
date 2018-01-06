package com.codenotfound.client;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ClientConfig {

  @Value("${client.address}")
  private String address;

  @Value("${client.trust-store}")
  private Resource trustStoreResource;

  @Value("${client.trust-store-password}")
  private String trustStorePassword;

  @Bean
  public TicketAgent ticketAgentProxy() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TicketAgent.class);
    jaxWsProxyFactoryBean.setAddress(address);

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }

  @Bean
  public HTTPConduit ticketAgentConduit()
      throws NoSuchAlgorithmException, KeyStoreException,
      CertificateException, IOException {
    Client client = ClientProxy.getClient(ticketAgentProxy());

    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    httpConduit.setTlsClientParameters(tlsClientParameters());

    return httpConduit;
  }

  @Bean
  public TLSClientParameters tlsClientParameters()
      throws NoSuchAlgorithmException, KeyStoreException,
      CertificateException, IOException {
    TLSClientParameters tlsClientParameters =
        new TLSClientParameters();
    tlsClientParameters.setSecureSocketProtocol("TLS");
    // should NOT be used in production
    tlsClientParameters.setDisableCNCheck(true);
    tlsClientParameters.setTrustManagers(trustManagers());
    tlsClientParameters.setCipherSuitesFilter(cipherSuitesFilter());

    return tlsClientParameters;
  }

  @Bean
  public TrustManager[] trustManagers()
      throws NoSuchAlgorithmException, KeyStoreException,
      CertificateException, IOException {
    TrustManagerFactory trustManagerFactory = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore());

    return trustManagerFactory.getTrustManagers();
  }

  @Bean
  public KeyStore trustStore() throws KeyStoreException,
      NoSuchAlgorithmException, CertificateException, IOException {
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(trustStoreResource.getInputStream(),
        trustStorePassword.toCharArray());

    return trustStore;
  }

  @Bean
  public FiltersType cipherSuitesFilter() {
    FiltersType filter = new FiltersType();
    filter.getInclude().add("TLS_ECDHE_RSA_.*");
    filter.getInclude().add("TLS_DHE_RSA_.*");

    return filter;
  }
}
