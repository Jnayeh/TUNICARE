package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationStatus;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class NotificationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(nullable = false)
  private String title;

  @Column private String body;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> data;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationChannel channel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "sent_at")
  private Instant sentAt;

  @Column(name = "read_at")
  private Instant readAt;

  @PrePersist
  void prePersist() {

    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
