package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "center_admin_profiles")
@Getter
@Setter
public class CenterAdminProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name = "center_id", nullable = false)
  private Long centerId;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {

    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
