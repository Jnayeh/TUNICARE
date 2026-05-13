package com.qatra.donationplatform.profiles.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


/** Class diagram: DonorProfile */
public class DonorProfile {

  private Long id;
  private Long userId;
  private BloodType bloodType;
  private AvailabilityStatus availability;
  private Double latitude;
  private Double longitude;
  private String address;
  private String city;
  private String country;
  private Map<String, Object> notificationPreferences = new HashMap<>();
  private Integer maxNotificationDistance;
  private NotificationFrequency notificationFrequency;
  private boolean allowEmergencyNotifications = true;
  private LocalDate lastDonationDate;
  private LocalDate eligibleFromDate;
  private int totalDonations;
  private int totalMlDonated;
  private double reliabilityScore = 100d;
  private Map<String, Object> healthQuestionnaire = new HashMap<>();
  private boolean profileComplete;
  private boolean permanentRestriction;
  private Instant createdAt;
  private Instant updatedAt;

  public boolean canDonate(LocalDate onDate) {
    if (permanentRestriction) {
      return false;
    }
    if (availability == AvailabilityStatus.PERMANENTLY_RESTRICTED) {
      return false;
    }
    if (eligibleFromDate != null && eligibleFromDate.isAfter(onDate)) {
      return false;
    }
    return true;
  }

  public void recordDonation(int mlCollected, LocalDate donationDay, int cooldownDays) {
    this.lastDonationDate = donationDay;
    this.eligibleFromDate = donationDay.plusDays(cooldownDays);
    this.totalDonations++;
    this.totalMlDonated += mlCollected;
  }

  public void calculateEligibility(LocalDate today) {
    if (permanentRestriction) {
      return;
    }
    if (eligibleFromDate != null && !eligibleFromDate.isAfter(today)) {
      /* eligibility restored — no separate flag in diagram */
    }
  }

  public void calculateReliability(double delta) {
    this.reliabilityScore = Math.max(0d, Math.min(100d, reliabilityScore + delta));
  }

  public void updateLocation(Double latitude, Double longitude, String address, String city, String country) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.address = address;
    this.city = city;
    this.country = country;
  }

  public void updateNotificationPreferences(
      Map<String, Object> prefs, NotificationFrequency frequency) {
    if (prefs != null) {
      this.notificationPreferences = new HashMap<>(prefs);
    }
    if (frequency != null) {
      this.notificationFrequency = frequency;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public BloodType getBloodType() {
    return bloodType;
  }

  public void setBloodType(BloodType bloodType) {
    this.bloodType = bloodType;
  }

  public AvailabilityStatus getAvailability() {
    return availability;
  }

  public void setAvailability(AvailabilityStatus availability) {
    this.availability = availability;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Map<String, Object> getNotificationPreferences() {
    return notificationPreferences;
  }

  public void setNotificationPreferences(Map<String, Object> notificationPreferences) {
    this.notificationPreferences =
        notificationPreferences != null ? new HashMap<>(notificationPreferences) : new HashMap<>();
  }

  public Integer getMaxNotificationDistance() {
    return maxNotificationDistance;
  }

  public void setMaxNotificationDistance(Integer maxNotificationDistance) {
    this.maxNotificationDistance = maxNotificationDistance;
  }

  public NotificationFrequency getNotificationFrequency() {
    return notificationFrequency;
  }

  public void setNotificationFrequency(NotificationFrequency notificationFrequency) {
    this.notificationFrequency = notificationFrequency;
  }

  public boolean isAllowEmergencyNotifications() {
    return allowEmergencyNotifications;
  }

  public void setAllowEmergencyNotifications(boolean allowEmergencyNotifications) {
    this.allowEmergencyNotifications = allowEmergencyNotifications;
  }

  public LocalDate getLastDonationDate() {
    return lastDonationDate;
  }

  public void setLastDonationDate(LocalDate lastDonationDate) {
    this.lastDonationDate = lastDonationDate;
  }

  public LocalDate getEligibleFromDate() {
    return eligibleFromDate;
  }

  public void setEligibleFromDate(LocalDate eligibleFromDate) {
    this.eligibleFromDate = eligibleFromDate;
  }

  public int getTotalDonations() {
    return totalDonations;
  }

  public void setTotalDonations(int totalDonations) {
    this.totalDonations = totalDonations;
  }

  public int getTotalMlDonated() {
    return totalMlDonated;
  }

  public void setTotalMlDonated(int totalMlDonated) {
    this.totalMlDonated = totalMlDonated;
  }

  public double getReliabilityScore() {
    return reliabilityScore;
  }

  public void setReliabilityScore(double reliabilityScore) {
    this.reliabilityScore = reliabilityScore;
  }

  public Map<String, Object> getHealthQuestionnaire() {
    return healthQuestionnaire;
  }

  public void setHealthQuestionnaire(Map<String, Object> healthQuestionnaire) {
    this.healthQuestionnaire =
        healthQuestionnaire != null ? new HashMap<>(healthQuestionnaire) : new HashMap<>();
  }

  public boolean isProfileComplete() {
    return profileComplete;
  }

  public void setProfileComplete(boolean profileComplete) {
    this.profileComplete = profileComplete;
  }

  public boolean isPermanentRestriction() {
    return permanentRestriction;
  }

  public void setPermanentRestriction(boolean permanentRestriction) {
    this.permanentRestriction = permanentRestriction;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
