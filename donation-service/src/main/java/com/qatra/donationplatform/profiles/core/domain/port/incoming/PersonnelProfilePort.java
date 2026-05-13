package com.qatra.donationplatform.profiles.core.domain.port.incoming;

import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import java.util.List;


public interface PersonnelProfilePort {

  CenterStaffProfile requireStaffForUser(Long staffUserId);

  CenterAdminProfile requireAdminForUser(Long adminUserId);

  CenterStaffProfile addStaff(
      Long adminUserId,
      Long centerId,
      String email,
      String hashedPassword,
      String displayName,
      String department);

  void removeStaff(Long adminUserId, Long centerId, Long staffProfileId);

  List<CenterStaffProfile> listStaff(Long adminUserId, Long centerId);
}
