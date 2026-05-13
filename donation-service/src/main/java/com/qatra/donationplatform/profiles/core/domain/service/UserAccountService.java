package com.qatra.donationplatform.profiles.core.domain.service;

import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.profiles.core.domain.model.UserRole;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.UserAccountPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRoleRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import com.qatra.donationplatform.shared.domain.enums.Role;
import com.qatra.donationplatform.shared.domain.enums.UserStatus;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccountService implements UserAccountPort {

  private final UserRepositoryPort users;
  private final UserRoleRepositoryPort roles;
  private final DonorProfileRepositoryPort donorProfiles;

  public UserAccountService(
      UserRepositoryPort users, UserRoleRepositoryPort roles, DonorProfileRepositoryPort donorProfiles) {
    this.users = users;
    this.roles = roles;
    this.donorProfiles = donorProfiles;
  }

  @Override
  @Transactional
  public Long registerDonorUser(
      String email, String phone, String hashedPassword, String displayName) {
    User u = new User();
    u.setEmail(email);
    u.setPhone(phone);
    u.setHashedPassword(hashedPassword);
    u.setDisplayName(displayName);
    u.setStatus(UserStatus.PENDING_VERIFICATION);
    u.setEmailVerified(false);
    u = users.save(u);
    UserRole r = new UserRole();
    r.setUserId(u.getId());
    r.setRole(Role.DONOR);
    r.setContextType(null);
    r.setAssignedAt(Instant.now());
    roles.save(r);
    DonorProfile d = new DonorProfile();
    d.setUserId(u.getId());
    d.setAvailability(AvailabilityStatus.AVAILABLE);
    d.setNotificationFrequency(NotificationFrequency.IMMEDIATE);
    d.setAllowEmergencyNotifications(true);
    donorProfiles.save(d);
    return u.getId();
  }

  @Override
  @Transactional
  public User updatePersonal(Long userId, String displayName, String email, String phone) {
    User u = requireUser(userId);
    if (displayName != null) {
      u.setDisplayName(displayName);
    }
    if (email != null) {
      u.setEmail(email);
    }
    if (phone != null) {
      u.setPhone(phone);
    }
    return users.save(u);
  }

  @Override
  @Transactional
  public void verifyEmail(Long userId) {
    User u = requireUser(userId);
    u.verifyEmail();
    users.save(u);
  }

  @Override
  @Transactional
  public void setStatus(Long userId, UserStatus status) {
    User u = requireUser(userId);
    u.setStatus(status);
    users.save(u);
  }

  @Override
  @Transactional
  public void assignRole(Long userId, Role role, Long contextId, String contextType) {
    UserRole r = new UserRole();
    r.setUserId(userId);
    r.setRole(role);
    r.setContextId(contextId);
    r.setContextType(contextType);
    r.setAssignedAt(Instant.now());
    roles.save(r);
  }

  @Override
  @Transactional
  public void revokeRole(Long roleAssignmentId) {
    roles.deleteById(roleAssignmentId);
  }

  @Override
  public User requireUser(Long userId) {
    return users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
  }
}
