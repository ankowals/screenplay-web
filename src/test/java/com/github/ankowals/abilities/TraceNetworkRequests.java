package com.github.ankowals.abilities;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.ResponseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record TraceNetworkRequests(Network network, List<ResponseDetails> responses)
    implements Ability {

  private static final Logger LOGGER = LoggerFactory.getLogger(TraceNetworkRequests.class);

  public TraceNetworkRequests {
    if (network == null) {
      throw new IllegalArgumentException("LogInspector cannot be null");
    }

    network.onResponseStarted(
        response -> {
          if (response.getResponseData().getStatus() > 399) {
            LOGGER.error(TraceNetworkRequests.toLogMessage(response));
          }
          responses.add(response);
        });
  }

  public static TraceNetworkRequests with(Network network) {
    return new TraceNetworkRequests(network, new ArrayList<>());
  }

  public static List<ResponseDetails> as(Actor actor) {
    return UseAbility.of(actor).to(TraceNetworkRequests.class).responses();
  }

  public List<ResponseDetails> responses() {
    this.network.close();
    return this.responses;
  }

  private static String toLogMessage(ResponseDetails responseDetails) {
    return "at: %s, method: %s, url: %s, status: %s"
        .formatted(
            TraceNetworkRequests.extractTimestamp(responseDetails),
            responseDetails.getRequest().getMethod(),
            responseDetails.getResponseData().getUrl(),
            responseDetails.getResponseData().getStatus());
  }

  private static LocalDateTime extractTimestamp(ResponseDetails responseDetails) {
    return Instant.ofEpochMilli(responseDetails.getTimestamp())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
