package com.qatra.donationplatform.profiles.core.domain.service;

import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.profiles.core.domain.model.UserRole;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.PersonnelProfilePort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterAdminProfileRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterStaffProfileRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRoleRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.Role;
import com.qatra.donationplatform.shared.domain.enums.UserStatus;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonnelProfileService implements PersonnelProfilePort {

  private final CenterAdminProfileRepositoryPort admins;
  private final CenterStaffProfileRepositoryPort staff;
  private final UserRepositoryPort users;
  private final UserRoleRepositoryPort roles;

  public PersonnelProfileService(
      CenterAdminProfileRepositoryPort admins,
      CenterStaffProfileRepositoryPort staff,
      UserRepositoryPort users,
      UserRoleRepositoryPort roles) {
    this.admins = admins;
    this.staff = staff;
    this.users = users;
    this.roles = roles;
  }

  @Override
  public CenterStaffProfile requireStaffForUser(Long staffUserId) {
    return staff
        .findByUserId(staffUserId)
        .orElseThrow(() -> new NotFoundException("Staff profile not found"));
  }

  @Override
  public CenterAdminProfile requireAdminForUser(Long adminUserId) {
    return admins
        .findByUserId(adminUserId)
        .orElseThrow(() -> new NotFoundException("Center admin profile not found"));
  }

  @Override
  @Transactional
  public CenterStaffProfile addStaff(
      Long adminUserId,
      Long centerId,
      String email,
      String hashedPassword,
      String displayName,
      String department) {
    requireAdminOf(adminUserId, centerId);
    User u = new User();
    u.setEmail(email);
    u.setHashedPassword(hashedPassword);
    u.setDisplayName(displayName);
    u.setStatus(UserStatus.ACTIVE);
    u.setEmailVerified(false);
    u = users.save(u);
    UserRole r = new UserRole();
    r.setUserId(u.getId());
    r.setRole(Role.CENTER_STAFF);
    r.setContextId(centerId);
    r.setContextType("CENTER");
    r.setAssignedAt(Instant.now());
    roles.save(r);
    CenterStaffProfile s = new CenterStaffProfile();
    s.setUserId(u.getId());
    s.setCenterId(centerId);
    s.setDepartment(department);
    return staff.save(s);
  }

  @Override
  @Transactional
  public void removeStaff(Long adminUserId, Long centerId, Long staffProfileId) {
    requireAdminOf(adminUserId, centerId);
    CenterStaffProfile s =
        staff.findById(staffProfileId).orElseThrow(() -> new NotFoundException("Staff not found"));
    if (!s.getCenterId().equals(centerId)) {
      throw new IllegalArgumentException("Staff not at center");
    }
    staff.delete(s);
  }

  @Override
  public List<CenterStaffProfile> listStaff(Long adminUserId, Long centerId) {
    requireAdminOf(adminUserId, centerId);
    return staff.findByCenterId(centerId);
  }

  private void requireAdminOf(Long adminUserId, Long centerId) {
    CenterAdminProfile a =
        admins.findByUserId(adminUserId).orElseThrow(() -> new NotFoundException("Admin not found"));
    if (!a.getCenterId().equals(centerId)) {
      throw new IllegalArgumentException("Admin does not manage this center");
    }
  }
}
