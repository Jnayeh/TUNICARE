package com.qatra.donationplatform.appointment.infrastructure.adapter.incoming.web;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.appointment.core.domain.port.incoming.AppointmentSchedulingPort;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.PersonnelProfilePort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff/appointments")
public class AppointmentStaffWebController {

  private final PersonnelProfilePort personnel;
  private final AppointmentSchedulingPort appointments;

  public AppointmentStaffWebController(
      PersonnelProfilePort personnel, AppointmentSchedulingPort appointments) {
    this.personnel = personnel;
    this.appointments = appointments;
  }

  @GetMapping("/schedule")
  ResponseEntity<List<Appointment>> schedule(
      @RequestHeader("X-User-Id") Long staffUserId,
      @RequestParam Instant from,
      @RequestParam Instant to) {
    var staff = personnel.requireStaffForUser(staffUserId);
    return ResponseEntity.ok(appointments.centerSchedule(staff.getCenterId(), from, to));
  }

  @PostMapping("/{appointmentId}/complete")
  ResponseEntity<Appointment> complete(
      @RequestHeader("X-User-Id") Long staffUserId,
      @PathVariable Long appointmentId,
      @RequestBody Map<String, Object> body) {
    int ml =
        body.get("mlCollected") instanceof Number n ? n.intValue() : 0;
    String notes = body.get("notes") instanceof String s ? s : null;
    return ResponseEntity.ok(
        appointments.completeByStaff(staffUserId, appointmentId, ml, notes));
  }

  @PostMapping("/{appointmentId}/no-show")
  ResponseEntity<Appointment> noShow(
      @RequestHeader("X-User-Id") Long staffUserId, @PathVariable Long appointmentId) {
    return ResponseEntity.ok(appointments.markNoShow(staffUserId, appointmentId));
  }

  public record WalkInBody(Long donorProfileId) {}

  @PostMapping("/walk-in")
  ResponseEntity<Appointment> walkIn(
      @RequestHeader("X-User-Id") Long staffUserId, @RequestBody WalkInBody body) {
    var staff = personnel.requireStaffForUser(staffUserId);
    return ResponseEntity.ok(
        appointments.walkIn(staffUserId, body.donorProfileId(), staff.getCenterId()));
  }
}
