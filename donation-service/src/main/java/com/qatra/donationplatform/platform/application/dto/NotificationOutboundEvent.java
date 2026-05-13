package com.qatra.donationplatform.platform.application.dto;

import java.util.Map;


public record NotificationOutboundEvent(
    Long notificationId,
    Long userId,
    String notificationType,
    String channel,
    String title,
    String body,
    Map<String, Object> data) {}
