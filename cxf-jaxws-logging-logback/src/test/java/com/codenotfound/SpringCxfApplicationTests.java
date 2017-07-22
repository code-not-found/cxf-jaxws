package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.example.ticketagent_wsdl11.ListFlightsFault;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.client.TicketAgentClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringCxfApplicationTests {

  @Autowired
  private TicketAgentClient ticketAgentClient;

  @Test
  public void testListFlights() throws ListFlightsFault {
    assertThat(ticketAgentClient.listFlights("LAX", "SFO").get(0))
        .isEqualTo(BigInteger.valueOf(101));
  }

  @Test
  public void testListFlightsFault() {
    try {
      ticketAgentClient.listFlights("XYZ", "SFO");
    } catch (Exception exception) {
      assertThat(exception.getMessage()).isEqualTo("unknown city");
    }
  }
}
