package com.qatra.donationplatform.center.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class CenterPersistenceMapper {

  public BloodDonationCenter toDomain(BloodDonationCenterEntity e) {
    BloodDonationCenter c = new BloodDonationCenter();
    c.setId(e.getId());
    c.setName(e.getName());
    c.setLatitude(e.getLatitude());
    c.setLongitude(e.getLongitude());
    c.setAddress(e.getAddress());
    c.setCity(e.getCity());
    c.setCountry(e.getCountry());
    c.setPostalCode(e.getPostalCode());
    c.setPhone(e.getPhone());
    c.setEmail(e.getEmail());
    c.setOperatingHours(
        e.getOperatingHours() != null ? new HashMap<>(e.getOperatingHours()) : new HashMap<>());
    c.setDailyCapacity(e.getDailyCapacity());
    c.setFacilityType(e.getFacilityType());
    c.setAmenities(e.getAmenities() != null ? new HashMap<>(e.getAmenities()) : new HashMap<>());
    c.setActive(e.isActive());
    c.setVerified(e.isVerified());
    c.setCreatedAt(e.getCreatedAt());
    c.setCreatedByUserId(e.getCreatedByUserId());
    return c;
  }

  public BloodDonationCenterEntity toEntity(BloodDonationCenter c) {
    BloodDonationCenterEntity e = new BloodDonationCenterEntity();
    e.setId(c.getId());
    e.setName(c.getName());
    e.setLatitude(c.getLatitude());
    e.setLongitude(c.getLongitude());
    e.setAddress(c.getAddress());
    e.setCity(c.getCity());
    e.setCountry(c.getCountry());
    e.setPostalCode(c.getPostalCode());
    e.setPhone(c.getPhone());
    e.setEmail(c.getEmail());
    e.setOperatingHours(
        c.getOperatingHours() != null ? new HashMap<>(c.getOperatingHours()) : new HashMap<>());
    e.setDailyCapacity(c.getDailyCapacity());
    e.setFacilityType(c.getFacilityType());
    e.setAmenities(c.getAmenities() != null ? new HashMap<>(c.getAmenities()) : new HashMap<>());
    e.setActive(c.isActive());
    e.setVerified(c.isVerified());
    e.setCreatedAt(c.getCreatedAt());
    e.setCreatedByUserId(c.getCreatedByUserId());
    return e;
  }
}
