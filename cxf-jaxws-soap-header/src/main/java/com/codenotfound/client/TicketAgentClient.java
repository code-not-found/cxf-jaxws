package com.codenotfound.client;

import java.math.BigInteger;
import java.util.List;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent.TListFlightsHeader;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketAgentClient {

  @Autowired
  private TicketAgent ticketAgentProxy;

  public List<BigInteger> listFlights(String clientId) {
    ObjectFactory factory = new ObjectFactory();
    TListFlights tListFlights = factory.createTListFlights();

    // create the SOAP header
    TListFlightsHeader tListFlightsHeader =
        factory.createTListFlightsHeader();
    tListFlightsHeader.setClientId(clientId);

    TFlightsResponse response = ticketAgentProxy
        .listFlights(tListFlights, tListFlightsHeader);

    return response.getFlightNumber();
  }
}
