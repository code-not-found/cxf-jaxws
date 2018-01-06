package com.codenotfound.endpoint;

import java.math.BigInteger;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent_wsdl11.TicketAgent;

public class TicketAgentImpl implements TicketAgent {

  @Override
  public TFlightsResponse listFlights(TListFlights body) {
    ObjectFactory factory = new ObjectFactory();
    TFlightsResponse response = factory.createTFlightsResponse();
    response.getFlightNumber().add(BigInteger.valueOf(101));

    return response;
  }
}
