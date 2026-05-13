package com.qatra.donationplatform.center.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.FacilityType;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/** Class diagram: BloodDonationCenter */
public class BloodDonationCenter {

  private Long id;
  private String name;
  private Double latitude;
  private Double longitude;
  private String address;
  private String city;
  private String country;
  private String postalCode;
  private String phone;
  private String email;
  private Map<String, Object> operatingHours = new HashMap<>();
  private int dailyCapacity;
  private FacilityType facilityType;
  private Map<String, Object> amenities = new HashMap<>();
  private boolean active = true;
  private boolean verified;
  private Instant createdAt;
  private Long createdByUserId;

  public boolean isOperatingNow() {
    return active;
  }

  public boolean hasCapacity() {
    return dailyCapacity > 0;
  }

  // getAvailableSlots() — depends on appointment module; exposed via application service

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Map<String, Object> getOperatingHours() {
    return operatingHours;
  }

  public void setOperatingHours(Map<String, Object> operatingHours) {
    this.operatingHours =
        operatingHours != null ? new HashMap<>(operatingHours) : new HashMap<>();
  }

  public int getDailyCapacity() {
    return dailyCapacity;
  }

  public void setDailyCapacity(int dailyCapacity) {
    this.dailyCapacity = dailyCapacity;
  }

  public FacilityType getFacilityType() {
    return facilityType;
  }

  public void setFacilityType(FacilityType facilityType) {
    this.facilityType = facilityType;
  }

  public Map<String, Object> getAmenities() {
    return amenities;
  }

  public void setAmenities(Map<String, Object> amenities) {
    this.amenities = amenities != null ? new HashMap<>(amenities) : new HashMap<>();
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Long getCreatedByUserId() {
    return createdByUserId;
  }

  public void setCreatedByUserId(Long createdByUserId) {
    this.createdByUserId = createdByUserId;
  }
}
