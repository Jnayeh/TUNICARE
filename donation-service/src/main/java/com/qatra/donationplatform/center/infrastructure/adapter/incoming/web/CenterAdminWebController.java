package com.qatra.donationplatform.center.infrastructure.adapter.incoming.web;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.incoming.CenterManagementPort;
import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.PersonnelProfilePort;
import com.qatra.donationplatform.shared.domain.enums.FacilityType;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/centers")
public class CenterAdminWebController {

  private final CenterManagementPort centers;
  private final PersonnelProfilePort personnel;

  public CenterAdminWebController(CenterManagementPort centers, PersonnelProfilePort personnel) {
    this.centers = centers;
    this.personnel = personnel;
  }

  public record RegisterCenterBody(
      String name,
      FacilityType facilityType,
      String address,
      String city,
      String country,
      String postalCode,
      double latitude,
      double longitude,
      int dailyCapacity) {}

  @PostMapping
  ResponseEntity<BloodDonationCenter> register(
      @RequestHeader("X-User-Id") Long adminUserId, @RequestBody RegisterCenterBody body) {
    return ResponseEntity.ok(
        centers.registerCenter(
            adminUserId,
            body.name(),
            body.facilityType(),
            body.address(),
            body.city(),
            body.country(),
            body.postalCode(),
            body.latitude(),
            body.longitude(),
            body.dailyCapacity()));
  }

  @GetMapping("/mine")
  ResponseEntity<BloodDonationCenter> myCenter(@RequestHeader("X-User-Id") Long adminUserId) {
    var admin = personnel.requireAdminForUser(adminUserId);
    return ResponseEntity.ok(centers.requireCenter(admin.getCenterId()));
  }

  @PatchMapping("/{centerId}")
  ResponseEntity<BloodDonationCenter> update(
      @RequestHeader("X-User-Id") Long adminUserId,
      @PathVariable Long centerId,
      @RequestBody Map<String, Object> body) {
    @SuppressWarnings("unchecked")
    Map<String, Object> hours =
        body.get("operatingHours") instanceof Map<?, ?> m ? (Map<String, Object>) m : null;
    Integer cap = body.get("dailyCapacity") instanceof Number n ? n.intValue() : null;
    return ResponseEntity.ok(
        centers.updateCenter(
            adminUserId,
            centerId,
            (String) body.get("address"),
            (String) body.get("phone"),
            (String) body.get("email"),
            hours,
            cap));
  }

  public record AddStaffBody(String email, String hashedPassword, String displayName, String department) {}

  @PostMapping("/{centerId}/staff")
  ResponseEntity<CenterStaffProfile> addStaff(
      @RequestHeader("X-User-Id") Long adminUserId,
      @PathVariable Long centerId,
      @RequestBody AddStaffBody body) {
    return ResponseEntity.ok(
        personnel.addStaff(
            adminUserId,
            centerId,
            body.email(),
            body.hashedPassword(),
            body.displayName(),
            body.department()));
  }

  @DeleteMapping("/{centerId}/staff/{staffProfileId}")
  ResponseEntity<Void> removeStaff(
      @RequestHeader("X-User-Id") Long adminUserId,
      @PathVariable Long centerId,
      @PathVariable Long staffProfileId) {
    personnel.removeStaff(adminUserId, centerId, staffProfileId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{centerId}/staff")
  ResponseEntity<List<CenterStaffProfile>> listStaff(
      @RequestHeader("X-User-Id") Long adminUserId, @PathVariable Long centerId) {
    return ResponseEntity.ok(personnel.listStaff(adminUserId, centerId));
  }
}
