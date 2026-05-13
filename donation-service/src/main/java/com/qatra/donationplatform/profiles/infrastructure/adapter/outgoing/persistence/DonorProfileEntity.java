package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "donor_profiles")
@Getter
@Setter
public class DonorProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Enumerated(EnumType.STRING)
  private BloodType bloodType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AvailabilityStatus availability;

  private Double latitude;
  private Double longitude;
  private String address;
  private String city;
  private String country;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "notification_preferences")
  private Map<String, Object> notificationPreferences;

  @Column(name = "max_notification_distance")
  private Integer maxNotificationDistance;

  @Enumerated(EnumType.STRING)
  @Column(name = "notification_frequency", nullable = false)
  private NotificationFrequency notificationFrequency;

  @Column(name = "allow_emergency_notifications", nullable = false)
  private boolean allowEmergencyNotifications = true;

  @Column(name = "last_donation_date")
  private LocalDate lastDonationDate;

  @Column(name = "eligible_from_date")
  private LocalDate eligibleFromDate;

  @Column(name = "total_donations", nullable = false)
  private int totalDonations;

  @Column(name = "total_ml_donated", nullable = false)
  private int totalMlDonated;

  @Column(name = "reliability_score", nullable = false)
  private double reliabilityScore = 100d;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "health_questionnaire")
  private Map<String, Object> healthQuestionnaire;

  @Column(name = "profile_complete", nullable = false)
  private boolean profileComplete;

  @Column(name = "permanent_restriction", nullable = false)
  private boolean permanentRestriction;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void prePersist() {

    Instant now = Instant.now();
    if (createdAt == null) {
      createdAt = now;
    }
    updatedAt = now;
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = Instant.now();
  }
}
