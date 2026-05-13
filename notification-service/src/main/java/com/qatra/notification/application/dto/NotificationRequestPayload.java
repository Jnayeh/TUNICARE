package com.qatra.notification.application.dto;

import io.micronaut.serde.annotation.Serdeable;
import java.util.Map;
import java.util.UUID;

@Serdeable
public record NotificationRequestPayload(
    UUID notificationId,
    UUID userId,
    String notificationType,
    String channel,
    String title,
    String body,
    Map<String, Object> data) {}
