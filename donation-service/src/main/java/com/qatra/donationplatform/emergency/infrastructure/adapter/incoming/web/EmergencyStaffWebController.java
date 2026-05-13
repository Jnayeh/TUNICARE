package com.qatra.donationplatform.emergency.infrastructure.adapter.incoming.web;

import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.emergency.core.domain.port.incoming.EmergencyOperationsPort;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.EmergencyUrgency;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff/emergencies")
public class EmergencyStaffWebController {

  private final EmergencyOperationsPort emergencies;

  public EmergencyStaffWebController(EmergencyOperationsPort emergencies) {
    this.emergencies = emergencies;
  }

  @GetMapping
  ResponseEntity<List<Emergency>> list(@RequestHeader("X-User-Id") Long staffUserId) {
    return ResponseEntity.ok(emergencies.listEmergenciesForStaffUser(staffUserId));
  }

  public record CreateEmergencyBody(
      BloodType bloodTypeNeeded,
      int unitsNeeded,
      EmergencyUrgency urgency,
      Instant neededBy,
      Integer matchRadiusKm,
      String contactPerson,
      String contactPhone,
      String patientInfo) {}

  @PostMapping
  ResponseEntity<Emergency> create(
      @RequestHeader("X-User-Id") Long staffUserId, @RequestBody CreateEmergencyBody body) {
    return ResponseEntity.ok(
        emergencies.create(
            staffUserId,
            null,
            body.bloodTypeNeeded(),
            body.unitsNeeded(),
            body.urgency(),
            body.neededBy(),
            body.matchRadiusKm(),
            body.contactPerson(),
            body.contactPhone(),
            body.patientInfo()));
  }

  @PostMapping("/{id}/escalate")
  ResponseEntity<Emergency> escalate(
      @RequestHeader("X-User-Id") Long staffUserId,
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(
        emergencies.escalateUrgency(
            staffUserId, id, EmergencyUrgency.valueOf(body.get("urgency"))));
  }

  @PostMapping("/{id}/extend")
  ResponseEntity<Emergency> extend(
      @RequestHeader("X-User-Id") Long staffUserId,
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(
        emergencies.extendDeadline(staffUserId, id, Instant.parse(body.get("neededBy"))));
  }

  @PostMapping("/{id}/cancel")
  ResponseEntity<Emergency> cancel(
      @RequestHeader("X-User-Id") Long staffUserId, @PathVariable Long id) {
    return ResponseEntity.ok(emergencies.cancel(staffUserId, id));
  }

  @PostMapping("/{id}/resolve")
  ResponseEntity<Emergency> resolve(
      @RequestHeader("X-User-Id") Long staffUserId, @PathVariable Long id) {
    return ResponseEntity.ok(emergencies.resolve(staffUserId, id));
  }
}
