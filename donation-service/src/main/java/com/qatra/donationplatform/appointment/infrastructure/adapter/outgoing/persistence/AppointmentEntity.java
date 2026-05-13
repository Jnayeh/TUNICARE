package com.qatra.donationplatform.appointment.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import com.qatra.donationplatform.shared.domain.enums.AppointmentType;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class AppointmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "donor_profile_id", nullable = false)
  private Long donorProfileId;

  @Column(name = "center_id", nullable = false)
  private Long centerId;

  @Column(name = "emergency_id")
  private Long emergencyId;

  @Column(name = "scheduled_time", nullable = false)
  private Instant scheduledTime;

  @Column(name = "estimated_duration", nullable = false)
  private int estimatedDuration = 60;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AppointmentStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "appointment_type", nullable = false)
  private AppointmentType appointmentType;

  @Column(name = "ml_collected")
  private Integer mlCollected;

  private String notes;

  @Column(name = "cancellation_reason")
  private String cancellationReason;

  @Column(name = "completed_by_staff_profile_id")
  private Long completedByStaffProfileId;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "confirmed_at")
  private Instant confirmedAt;

  @Column(name = "completed_at")
  private Instant completedAt;

  @Column(name = "cancelled_at")
  private Instant cancelledAt;

  @PrePersist
  void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
