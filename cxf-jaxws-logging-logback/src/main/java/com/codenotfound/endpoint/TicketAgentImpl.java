package com.codenotfound.endpoint;

import java.math.BigInteger;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsFault;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent_wsdl11.ListFlightsFault;
import org.example.ticketagent_wsdl11.TicketAgent;

public class TicketAgentImpl implements TicketAgent {

  @Override
  public TFlightsResponse listFlights(TListFlights request)
      throws ListFlightsFault {
    ObjectFactory factory = new ObjectFactory();

    if ("XYZ".equals(request.getStartCity())) {
      TFlightsFault tFlightsFault = factory.createTFlightsFault();
      tFlightsFault.setErrorMessage("no city named XYZ");

      throw new ListFlightsFault("unknown city", tFlightsFault);
    }

    TFlightsResponse response = factory.createTFlightsResponse();
    response.getFlightNumber().add(BigInteger.valueOf(101));

    return response;
  }
}
