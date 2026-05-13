package com.qatra.donationplatform.center.core.domain.port.incoming;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.shared.domain.enums.FacilityType;
import java.util.List;
import java.util.Map;


public interface CenterManagementPort {

  BloodDonationCenter registerCenter(
      Long adminUserId,
      String name,
      FacilityType facilityType,
      String address,
      String city,
      String country,
      String postalCode,
      double latitude,
      double longitude,
      int dailyCapacity);

  BloodDonationCenter updateCenter(
      Long adminUserId,
      Long centerId,
      String address,
      String phone,
      String email,
      Map<String, Object> operatingHours,
      Integer dailyCapacity);

  List<BloodDonationCenter> listActiveCenters();

  BloodDonationCenter requireCenter(Long centerId);

  void verifyCenter(Long centerId);
}
