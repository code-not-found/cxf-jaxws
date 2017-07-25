package com.codenotfound.endpoint;

import java.math.BigInteger;

import org.apache.cxf.feature.Features;
import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent_wsdl11.TicketAgent;

@Features(features = "org.apache.cxf.feature.LoggingFeature")
public class TicketAgentImpl implements TicketAgent {

  @Override
  public TFlightsResponse listFlights(TListFlights request) {
    ObjectFactory factory = new ObjectFactory();
    TFlightsResponse response = factory.createTFlightsResponse();
    response.getFlightNumber().add(BigInteger.valueOf(101));

    return response;
  }
}
