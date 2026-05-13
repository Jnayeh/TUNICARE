package com.qatra.donationplatform.profiles.core.domain.port.incoming;

import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.shared.domain.enums.UserStatus;


public interface UserAccountPort {

  Long registerDonorUser(String email, String phone, String hashedPassword, String displayName);

  User updatePersonal(Long userId, String displayName, String email, String phone);

  void verifyEmail(Long userId);

  void setStatus(Long userId, UserStatus status);

  void assignRole(
      Long userId,
      com.qatra.donationplatform.shared.domain.enums.Role role,
      Long contextId,
      String contextType);

  void revokeRole(Long roleAssignmentId);

  User requireUser(Long userId);
}
