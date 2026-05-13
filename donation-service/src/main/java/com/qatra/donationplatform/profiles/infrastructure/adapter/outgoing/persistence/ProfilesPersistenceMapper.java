package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.profiles.core.domain.model.UserRole;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import com.qatra.donationplatform.shared.domain.enums.Role;
import com.qatra.donationplatform.shared.domain.enums.UserStatus;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class ProfilesPersistenceMapper {

  public User toDomain(UserEntity e) {
    if (e == null) {
      return null;
    }
    User u = new User();
    u.setId(e.getId());
    u.setEmail(e.getEmail());
    u.setPhone(e.getPhone());
    u.setHashedPassword(e.getHashedPassword());
    u.setDisplayName(e.getDisplayName());
    u.setStatus(e.getStatus());
    u.setEmailVerified(e.isEmailVerified());
    u.setCreatedAt(e.getCreatedAt());
    u.setLastActiveAt(e.getLastActiveAt());
    return u;
  }

  public UserEntity toEntity(User u) {
    UserEntity e = new UserEntity();
    e.setId(u.getId());
    e.setEmail(u.getEmail());
    e.setPhone(u.getPhone());
    e.setHashedPassword(u.getHashedPassword());
    e.setDisplayName(u.getDisplayName());
    e.setStatus(u.getStatus());
    e.setEmailVerified(u.isEmailVerified());
    e.setCreatedAt(u.getCreatedAt());
    e.setLastActiveAt(u.getLastActiveAt());
    return e;
  }

  public UserRole toDomain(UserRoleEntity e) {
    UserRole r = new UserRole();
    r.setId(e.getId());
    r.setUserId(e.getUserId());
    r.setRole(e.getRole());
    r.setContextId(e.getContextId());
    r.setContextType(e.getContextType());
    r.setAssignedAt(e.getAssignedAt());
    return r;
  }

  public UserRoleEntity toEntity(UserRole r) {
    UserRoleEntity e = new UserRoleEntity();
    e.setId(r.getId());
    e.setUserId(r.getUserId());
    e.setRole(r.getRole());
    e.setContextId(r.getContextId());
    e.setContextType(r.getContextType());
    e.setAssignedAt(r.getAssignedAt());
    return e;
  }

  public DonorProfile toDomain(DonorProfileEntity e) {
    DonorProfile d = new DonorProfile();
    d.setId(e.getId());
    d.setUserId(e.getUserId());
    d.setBloodType(e.getBloodType());
    d.setAvailability(e.getAvailability());
    d.setLatitude(e.getLatitude());
    d.setLongitude(e.getLongitude());
    d.setAddress(e.getAddress());
    d.setCity(e.getCity());
    d.setCountry(e.getCountry());
    d.setNotificationPreferences(
        e.getNotificationPreferences() != null ? new HashMap<>(e.getNotificationPreferences()) : new HashMap<>());
    d.setMaxNotificationDistance(e.getMaxNotificationDistance());
    d.setNotificationFrequency(e.getNotificationFrequency());
    d.setAllowEmergencyNotifications(e.isAllowEmergencyNotifications());
    d.setLastDonationDate(e.getLastDonationDate());
    d.setEligibleFromDate(e.getEligibleFromDate());
    d.setTotalDonations(e.getTotalDonations());
    d.setTotalMlDonated(e.getTotalMlDonated());
    d.setReliabilityScore(e.getReliabilityScore());
    d.setHealthQuestionnaire(
        e.getHealthQuestionnaire() != null ? new HashMap<>(e.getHealthQuestionnaire()) : new HashMap<>());
    d.setProfileComplete(e.isProfileComplete());
    d.setPermanentRestriction(e.isPermanentRestriction());
    d.setCreatedAt(e.getCreatedAt());
    d.setUpdatedAt(e.getUpdatedAt());
    return d;
  }

  public DonorProfileEntity toEntity(DonorProfile d) {
    DonorProfileEntity e = new DonorProfileEntity();
    e.setId(d.getId());
    e.setUserId(d.getUserId());
    e.setBloodType(d.getBloodType());
    e.setAvailability(d.getAvailability());
    e.setLatitude(d.getLatitude());
    e.setLongitude(d.getLongitude());
    e.setAddress(d.getAddress());
    e.setCity(d.getCity());
    e.setCountry(d.getCountry());
    e.setNotificationPreferences(
        d.getNotificationPreferences() != null ? new HashMap<>(d.getNotificationPreferences()) : new HashMap<>());
    e.setMaxNotificationDistance(d.getMaxNotificationDistance());
    e.setNotificationFrequency(d.getNotificationFrequency());
    e.setAllowEmergencyNotifications(d.isAllowEmergencyNotifications());
    e.setLastDonationDate(d.getLastDonationDate());
    e.setEligibleFromDate(d.getEligibleFromDate());
    e.setTotalDonations(d.getTotalDonations());
    e.setTotalMlDonated(d.getTotalMlDonated());
    e.setReliabilityScore(d.getReliabilityScore());
    e.setHealthQuestionnaire(
        d.getHealthQuestionnaire() != null ? new HashMap<>(d.getHealthQuestionnaire()) : new HashMap<>());
    e.setProfileComplete(d.isProfileComplete());
    e.setPermanentRestriction(d.isPermanentRestriction());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }

  public CenterStaffProfile toDomain(CenterStaffProfileEntity e) {
    CenterStaffProfile s = new CenterStaffProfile();
    s.setId(e.getId());
    s.setUserId(e.getUserId());
    s.setCenterId(e.getCenterId());
    s.setDepartment(e.getDepartment());
    s.setVerified(e.isVerified());
    s.setCreatedAt(e.getCreatedAt());
    return s;
  }

  public CenterStaffProfileEntity toEntity(CenterStaffProfile s) {
    CenterStaffProfileEntity e = new CenterStaffProfileEntity();
    e.setId(s.getId());
    e.setUserId(s.getUserId());
    e.setCenterId(s.getCenterId());
    e.setDepartment(s.getDepartment());
    e.setVerified(s.isVerified());
    e.setCreatedAt(s.getCreatedAt());
    return e;
  }

  public CenterAdminProfile toDomain(CenterAdminProfileEntity e) {
    CenterAdminProfile a = new CenterAdminProfile();
    a.setId(e.getId());
    a.setUserId(e.getUserId());
    a.setCenterId(e.getCenterId());
    a.setCreatedAt(e.getCreatedAt());
    return a;
  }

  public CenterAdminProfileEntity toEntity(CenterAdminProfile a) {
    CenterAdminProfileEntity e = new CenterAdminProfileEntity();
    e.setId(a.getId());
    e.setUserId(a.getUserId());
    e.setCenterId(a.getCenterId());
    e.setCreatedAt(a.getCreatedAt());
    return e;
  }
}
