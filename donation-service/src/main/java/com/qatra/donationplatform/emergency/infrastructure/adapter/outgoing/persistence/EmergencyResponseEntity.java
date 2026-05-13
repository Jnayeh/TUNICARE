package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.ResponseType;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emergency_responses")
@Getter
@Setter
public class EmergencyResponseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "emergency_id", nullable = false)
  private Long emergencyId;

  @Column(name = "donor_profile_id", nullable = false)
  private Long donorProfileId;

  @Enumerated(EnumType.STRING)
  @Column(name = "response_type", nullable = false)
  private ResponseType responseType;

  private String message;

  @Column(name = "responded_at")
  private Instant respondedAt;

  @Column(name = "notified_at", nullable = false)
  private Instant notifiedAt;

  @PrePersist
  void prePersist() {

    if (notifiedAt == null) {
      notifiedAt = Instant.now();
    }
  }
}
