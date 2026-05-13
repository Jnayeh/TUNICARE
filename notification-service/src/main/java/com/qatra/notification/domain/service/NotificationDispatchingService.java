package com.qatra.notification.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qatra.notification.application.dto.NotificationRequestPayload;
import com.qatra.notification.infrastructure.persistence.JooqNotificationDeliveryRepository;
import com.qatra.notification.infrastructure.ws.NotificationHub;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates persistence (JOOQ), real-time fan-out (WebSocket), and future channel adapters (SMS,
 * email, push) without expanding scope beyond the provided use cases.
 */
@Singleton
public class NotificationDispatchingService {

  private static final Logger LOG = LoggerFactory.getLogger(NotificationDispatchingService.class);

  private final JooqNotificationDeliveryRepository deliveries;
  private final NotificationHub hub;
  private final ObjectMapper objectMapper;

  public NotificationDispatchingService(
      JooqNotificationDeliveryRepository deliveries,
      NotificationHub hub,
      ObjectMapper objectMapper) {
    this.deliveries = deliveries;
    this.hub = hub;
    this.objectMapper = objectMapper;
  }

  public void handle(NotificationRequestPayload event) {
    LOG.info(
        "Dispatch user={} type={} channel={} id={}",
        event.userId(),
        event.notificationType(),
        event.channel(),
        event.notificationId());
    String raw;
    try {
      raw = objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      raw = "{}";
    }
    deliveries.insertDelivery(
        event.notificationId(),
        event.userId(),
        event.channel(),
        "SENT",
        event.title(),
        event.body(),
        raw);
    hub.broadcast(
        event.userId().toString(),
        "{\"notificationId\":\""
            + event.notificationId()
            + "\",\"title\":"
            + jsonString(event.title())
            + "}");
  }

  private static String jsonString(String s) {
    if (s == null) {
      return "\"\"";
    }
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }
}
