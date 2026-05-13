package com.qatra.donationplatform.profiles.infrastructure.adapter.incoming.web;

import com.qatra.donationplatform.center.core.domain.port.incoming.CenterManagementPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.AuditJournalPort;
import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRepositoryPort;
import java.util.Collection;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/super")
public class SuperAdminWebController {

  private final UserRepositoryPort users;
  private final CenterManagementPort centers;
  private final AuditJournalPort audit;

  public SuperAdminWebController(
      UserRepositoryPort users, CenterManagementPort centers, AuditJournalPort audit) {
    this.users = users;
    this.centers = centers;
    this.audit = audit;
  }

  @GetMapping("/users")
  ResponseEntity<Collection<User>> listUsers() {
    return ResponseEntity.ok(users.findAll());
  }

  @PostMapping("/centers/{centerId}/verify")
  ResponseEntity<Void> verifyCenter(
      @RequestHeader("X-User-Id") Long superAdminUserId, @PathVariable Long centerId) {
    centers.verifyCenter(centerId);
    audit.record(
        superAdminUserId,
        "CENTER_VERIFIED",
        "BloodDonationCenter",
        centerId,
        Map.of("verified", false),
        Map.of("verified", true),
        null,
        null);
    return ResponseEntity.noContent().build();
  }
}
