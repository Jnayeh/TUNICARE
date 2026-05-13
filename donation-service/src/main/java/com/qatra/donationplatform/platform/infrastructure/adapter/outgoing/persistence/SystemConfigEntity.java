package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "system_config")
@Getter
@Setter
public class SystemConfigEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "config_key", nullable = false, unique = true)
  private String configKey;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config_value", nullable = false)
  private Map<String, Object> configValue;

  private String description;

  @Column(name = "is_active", nullable = false)
  private boolean active = true;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "updated_by_user_id")
  private Long updatedByUserId;

  @PrePersist
  void prePersist() {

    if (updatedAt == null) {
      updatedAt = Instant.now();
    }
  }
}
