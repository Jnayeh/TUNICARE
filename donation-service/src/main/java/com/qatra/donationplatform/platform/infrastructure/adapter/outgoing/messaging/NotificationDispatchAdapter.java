package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.messaging;

import com.qatra.donationplatform.platform.application.dto.NotificationOutboundEvent;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.NotificationDispatchPort;
import com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence.NotificationEntity;
import com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence.NotificationJpaRepository;
import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationStatus;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NotificationDispatchAdapter implements NotificationDispatchPort {

  private final NotificationJpaRepository notifications;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${app.kafka.topic.notification-requests}")
  private String topic;

  public NotificationDispatchAdapter(
      NotificationJpaRepository notifications, KafkaTemplate<String, Object> kafkaTemplate) {
    this.notifications = notifications;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  @Transactional
  public void dispatch(
      Long userId,
      NotificationType type,
      NotificationChannel channel,
      String title,
      String body,
      Map<String, Object> data) {
    NotificationEntity n = new NotificationEntity();
    n.setUserId(userId);
    n.setType(type);
    n.setTitle(title);
    n.setBody(body);
    n.setData(data != null ? data : new HashMap<>());
    n.setChannel(channel);
    n.setStatus(NotificationStatus.PENDING);
    notifications.save(n);
    NotificationOutboundEvent evt =
        new NotificationOutboundEvent(
            n.getId(), userId, type.name(), channel.name(), title, body, n.getData());
    kafkaTemplate.send(topic, userId.toString(), evt);
  }
}
