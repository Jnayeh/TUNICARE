package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(nullable = false)
  private String action;

  @Column(name = "entity_type", nullable = false)
  private String entityType;

  @Column(name = "entity_id")
  private Long entityId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "old_value")
  private Map<String, Object> oldValue;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "new_value")
  private Map<String, Object> newValue;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "user_agent")
  private String userAgent;

  @Column(nullable = false)
  private Instant timestamp;

  @PrePersist
  void prePersist() {

    if (timestamp == null) {
      timestamp = Instant.now();
    }
  }
}
