package com.qatra.donationplatform.center.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.FacilityType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "blood_donation_centers")
@Getter
@Setter
public class BloodDonationCenterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(nullable = false)
  private String name;

  private Double latitude;
  private Double longitude;
  private String address;
  private String city;
  private String country;

  @Column(name = "postal_code")
  private String postalCode;

  private String phone;
  private String email;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "operating_hours")
  private Map<String, Object> operatingHours;

  @Column(name = "daily_capacity", nullable = false)
  private int dailyCapacity;

  @Enumerated(EnumType.STRING)
  @Column(name = "facility_type", nullable = false)
  private FacilityType facilityType;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> amenities;

  @Column(name = "is_active", nullable = false)
  private boolean active = true;

  @Column(name = "is_verified", nullable = false)
  private boolean verified;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "created_by_user_id")
  private Long createdByUserId;

  @PrePersist
  void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
