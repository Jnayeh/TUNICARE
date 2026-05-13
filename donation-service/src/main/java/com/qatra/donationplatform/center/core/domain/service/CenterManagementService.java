package com.qatra.donationplatform.center.core.domain.service;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.incoming.CenterManagementPort;
import com.qatra.donationplatform.center.core.domain.port.outgoing.BloodDonationCenterRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterAdminProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.FacilityType;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CenterManagementService implements CenterManagementPort {

  private final BloodDonationCenterRepositoryPort centers;
  private final CenterAdminProfileRepositoryPort admins;

  public CenterManagementService(
      BloodDonationCenterRepositoryPort centers, CenterAdminProfileRepositoryPort admins) {
    this.centers = centers;
    this.admins = admins;
  }

  @Override
  @Transactional
  public BloodDonationCenter registerCenter(
      Long adminUserId,
      String name,
      FacilityType facilityType,
      String address,
      String city,
      String country,
      String postalCode,
      double latitude,
      double longitude,
      int dailyCapacity) {
    BloodDonationCenter c = new BloodDonationCenter();
    c.setName(name);
    c.setFacilityType(facilityType);
    c.setAddress(address);
    c.setCity(city);
    c.setCountry(country);
    c.setPostalCode(postalCode);
    c.setLatitude(latitude);
    c.setLongitude(longitude);
    c.setDailyCapacity(dailyCapacity);
    c.setCreatedByUserId(adminUserId);
    c.setVerified(false);
    c = centers.save(c);
    CenterAdminProfile link =
        admins.findByUserId(adminUserId).orElseGet(CenterAdminProfile::new);
    if (link.getUserId() == null) {
      link.setUserId(adminUserId);
    }
    link.setCenterId(c.getId());
    admins.save(link);
    return c;
  }

  @Override
  @Transactional
  public BloodDonationCenter updateCenter(
      Long adminUserId,
      Long centerId,
      String address,
      String phone,
      String email,
      Map<String, Object> operatingHours,
      Integer dailyCapacity) {
    requireAdminOf(adminUserId, centerId);
    BloodDonationCenter c = requireCenter(centerId);
    if (address != null) {
      c.setAddress(address);
    }
    if (phone != null) {
      c.setPhone(phone);
    }
    if (email != null) {
      c.setEmail(email);
    }
    if (operatingHours != null) {
      c.setOperatingHours(operatingHours);
    }
    if (dailyCapacity != null) {
      c.setDailyCapacity(dailyCapacity);
    }
    return centers.save(c);
  }

  @Override
  public List<BloodDonationCenter> listActiveCenters() {
    return centers.findAllActive();
  }

  @Override
  public BloodDonationCenter requireCenter(Long centerId) {
    return centers
        .findById(centerId)
        .orElseThrow(() -> new NotFoundException("Center not found"));
  }

  @Override
  @Transactional
  public void verifyCenter(Long centerId) {
    BloodDonationCenter c = requireCenter(centerId);
    c.setVerified(true);
    centers.save(c);
  }

  private void requireAdminOf(Long adminUserId, Long centerId) {
    CenterAdminProfile a =
        admins.findByUserId(adminUserId).orElseThrow(() -> new NotFoundException("Admin not found"));
    if (!a.getCenterId().equals(centerId)) {
      throw new IllegalArgumentException("Admin does not manage this center");
    }
  }
}
