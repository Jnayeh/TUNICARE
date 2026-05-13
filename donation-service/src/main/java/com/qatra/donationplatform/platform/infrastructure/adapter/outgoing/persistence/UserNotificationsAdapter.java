package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.platform.core.domain.model.Notification;
import com.qatra.donationplatform.platform.core.domain.port.incoming.UserNotificationsPort;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationsAdapter implements UserNotificationsPort {

  private final NotificationJpaRepository repo;

  public UserNotificationsAdapter(NotificationJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Notification> listRecentForUser(Long userId) {
    return repo.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(UserNotificationsAdapter::toDomain)
        .collect(Collectors.toList());
  }

  private static Notification toDomain(NotificationEntity e) {
    Notification n = new Notification();
    n.setId(e.getId());
    n.setUserId(e.getUserId());
    n.setType(e.getType());
    n.setTitle(e.getTitle());
    n.setBody(e.getBody());
    n.setData(e.getData() != null ? new HashMap<>(e.getData()) : new HashMap<>());
    n.setChannel(e.getChannel());
    n.setStatus(e.getStatus());
    n.setCreatedAt(e.getCreatedAt());
    n.setSentAt(e.getSentAt());
    n.setReadAt(e.getReadAt());
    return n;
  }
}
