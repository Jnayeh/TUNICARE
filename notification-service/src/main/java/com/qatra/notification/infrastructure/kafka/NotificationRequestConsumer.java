package com.qatra.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qatra.notification.application.dto.NotificationRequestPayload;
import com.qatra.notification.domain.service.NotificationDispatchingService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(
    groupId = "${qatra.kafka.consumer-group:notification-service}",
    clientId = "notification-consumer")
public class NotificationRequestConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(NotificationRequestConsumer.class);

  private final ObjectMapper objectMapper;
  private final NotificationDispatchingService dispatchingService;

  public NotificationRequestConsumer(
      ObjectMapper objectMapper, NotificationDispatchingService dispatchingService) {
    this.objectMapper = objectMapper;
    this.dispatchingService = dispatchingService;
  }

  @Topic("${qatra.kafka.notification-topic}")
  public void receive(String json) {
    try {
      NotificationRequestPayload payload = objectMapper.readValue(json, NotificationRequestPayload.class);
      dispatchingService.handle(payload);
    } catch (Exception e) {
      LOG.error("Failed to process notification payload: {}", e.toString());
    }
  }
}
