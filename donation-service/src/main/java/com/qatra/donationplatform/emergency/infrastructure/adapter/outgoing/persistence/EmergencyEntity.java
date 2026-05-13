package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import com.qatra.donationplatform.shared.domain.enums.EmergencyUrgency;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emergencies")
@Getter
@Setter
public class EmergencyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "center_id", nullable = false)
  private Long centerId;

  @Column(name = "created_by_staff_profile_id", nullable = false)
  private Long createdByStaffProfileId;

  @Enumerated(EnumType.STRING)
  @Column(name = "blood_type_needed", nullable = false)
  private BloodType bloodTypeNeeded;

  @Column(name = "units_needed", nullable = false)
  private int unitsNeeded;

  @Column(name = "units_collected", nullable = false)
  private int unitsCollected;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EmergencyUrgency urgency;

  @Column(name = "contact_person")
  private String contactPerson;

  @Column(name = "contact_phone")
  private String contactPhone;

  @Column(name = "patient_info")
  private String patientInfo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EmergencyStatus status;

  @Column(name = "match_radius")
  private Integer matchRadius;

  @Column(name = "needed_by")
  private Instant neededBy;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "resolved_at")
  private Instant resolvedAt;

  @Column(name = "resolved_by_staff_profile_id")
  private Long resolvedByStaffProfileId;

  @PrePersist
  void prePersist() {

    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
