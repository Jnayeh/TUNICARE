package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "center_staff_profiles")
@Getter
@Setter
public class CenterStaffProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name = "center_id", nullable = false)
  private Long centerId;

  private String department;

  @Column(name = "is_verified", nullable = false)
  private boolean verified;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {

    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
