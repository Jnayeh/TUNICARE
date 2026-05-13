package com.qatra.donationplatform.profiles.infrastructure.adapter.incoming.web;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.appointment.core.domain.port.incoming.AppointmentSchedulingPort;
import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.incoming.CenterManagementPort;
import com.qatra.donationplatform.donation.core.domain.port.incoming.DonationInsightsPort;
import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import com.qatra.donationplatform.emergency.core.domain.port.incoming.EmergencyOperationsPort;
import com.qatra.donationplatform.platform.core.domain.model.Notification;
import com.qatra.donationplatform.platform.core.domain.port.incoming.UserNotificationsPort;
import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.DonorProfilePort;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.UserAccountPort;
import com.qatra.donationplatform.shared.domain.enums.AppointmentType;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/donors")
public class DonorWebController {

  private final UserAccountPort users;
  private final DonorProfilePort donorProfile;
  private final AppointmentSchedulingPort appointments;
  private final EmergencyOperationsPort emergencies;
  private final CenterManagementPort centers;
  private final DonationInsightsPort donationInsights;
  private final UserNotificationsPort userNotifications;

  public DonorWebController(
      UserAccountPort users,
      DonorProfilePort donorProfile,
      AppointmentSchedulingPort appointments,
      EmergencyOperationsPort emergencies,
      CenterManagementPort centers,
      DonationInsightsPort donationInsights,
      UserNotificationsPort userNotifications) {
    this.users = users;
    this.donorProfile = donorProfile;
    this.appointments = appointments;
    this.emergencies = emergencies;
    this.centers = centers;
    this.donationInsights = donationInsights;
    this.userNotifications = userNotifications;
  }

  public record RegisterDonorRequest(
      String email, String phone, String hashedPassword, String displayName) {}

  @PostMapping("/register")
  ResponseEntity<Map<String, String>> register(@RequestBody RegisterDonorRequest body) {
    Long id =
        users.registerDonorUser(
            body.email(), body.phone(), body.hashedPassword(), body.displayName());
    return ResponseEntity.ok(Map.of("userId", id.toString()));
  }

  @PostMapping("/me/email/verify")
  ResponseEntity<Void> verifyEmail(@RequestHeader("X-User-Id") Long userId) {
    users.verifyEmail(userId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me/dashboard")
  ResponseEntity<DonorProfile> dashboard(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(donorProfile.requireForUser(userId));
  }

  @PatchMapping("/me/profile/personal")
  ResponseEntity<DonorProfile> personal(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, String> body) {
    users.updatePersonal(userId, body.get("displayName"), body.get("email"), body.get("phone"));
    return ResponseEntity.ok(donorProfile.requireForUser(userId));
  }

  @PatchMapping("/me/profile/health")
  ResponseEntity<DonorProfile> health(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, Object> questionnaire) {
    return ResponseEntity.ok(donorProfile.updateHealthQuestionnaire(userId, questionnaire));
  }

  @PatchMapping("/me/profile/location")
  ResponseEntity<DonorProfile> location(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, Object> body) {
    return ResponseEntity.ok(
        donorProfile.updateLocation(
            userId,
            body.get("latitude") instanceof Number n ? n.doubleValue() : null,
            body.get("longitude") instanceof Number n ? n.doubleValue() : null,
            (String) body.get("address"),
            (String) body.get("city"),
            (String) body.get("country")));
  }

  @PatchMapping("/me/profile/notifications")
  ResponseEntity<DonorProfile> notifications(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, Object> body) {
    NotificationFrequency freq =
        body.get("frequency") instanceof String s ? NotificationFrequency.valueOf(s) : null;
    @SuppressWarnings("unchecked")
    Map<String, Object> prefs = (Map<String, Object>) body.get("preferences");
    return ResponseEntity.ok(donorProfile.updateNotificationPreferences(userId, prefs, freq));
  }

  @PatchMapping("/me/availability")
  ResponseEntity<DonorProfile> availability(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(
        donorProfile.updateAvailability(userId, AvailabilityStatus.valueOf(body.get("status"))));
  }

  @PatchMapping("/me/blood-type")
  ResponseEntity<DonorProfile> blood(
      @RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, String> body) {
    BloodType bt =
        body.get("bloodType") == null || "UNKNOWN".equalsIgnoreCase(body.get("bloodType"))
            ? BloodType.UNKNOWN
            : BloodType.valueOf(body.get("bloodType"));
    return ResponseEntity.ok(donorProfile.setBloodType(userId, bt));
  }

  @GetMapping("/me/eligibility")
  ResponseEntity<Map<String, Object>> eligibility(@RequestHeader("X-User-Id") Long userId) {
    DonorProfile d = donorProfile.requireForUser(userId);
    boolean ok =
        d.getEligibleFromDate() == null || !d.getEligibleFromDate().isAfter(java.time.LocalDate.now());
    return ResponseEntity.ok(
        Map.of(
            "eligible",
            ok && !d.isPermanentRestriction(),
            "eligibleFromDate",
            d.getEligibleFromDate() != null ? d.getEligibleFromDate().toString() : "",
            "permanentRestriction",
            d.isPermanentRestriction()));
  }

  @GetMapping("/me/reliability")
  ResponseEntity<Map<String, Object>> reliability(@RequestHeader("X-User-Id") Long userId) {
    DonorProfile d = donorProfile.requireForUser(userId);
    return ResponseEntity.ok(Map.of("score", d.getReliabilityScore()));
  }

  @GetMapping("/me/impact")
  ResponseEntity<Map<String, Object>> impact(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(donationInsights.impactForDonor(userId));
  }

  @GetMapping("/me/certificates/latest")
  ResponseEntity<Map<String, String>> certificate(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(donationInsights.certificateLocation(userId));
  }

  @GetMapping("/centers/nearby")
  ResponseEntity<List<BloodDonationCenter>> nearby(
      @RequestHeader("X-User-Id") Long userId, @RequestParam(defaultValue = "50") double maxKm) {
    DonorProfile d = donorProfile.requireForUser(userId);
    List<BloodDonationCenter> all = centers.listActiveCenters();
    if (d.getLatitude() == null || d.getLongitude() == null) {
      return ResponseEntity.ok(all);
    }
    return ResponseEntity.ok(
        all.stream()
            .filter(
                c ->
                    com.qatra.donationplatform.shared.util.GeoUtils.distanceKm(
                            d.getLatitude(), d.getLongitude(), c.getLatitude(), c.getLongitude())
                        <= maxKm)
            .toList());
  }

  @GetMapping("/centers/{id}")
  ResponseEntity<BloodDonationCenter> center(@PathVariable Long id) {
    return ResponseEntity.ok(centers.requireCenter(id));
  }

  public record ScheduleBody(Long centerId, Instant scheduledTime, AppointmentType type, Long emergencyId) {}

  @PostMapping("/me/appointments")
  ResponseEntity<Appointment> scheduleAppt(
      @RequestHeader("X-User-Id") Long userId, @RequestBody ScheduleBody body) {
    return ResponseEntity.ok(
        appointments.schedule(
            userId, body.centerId(), body.scheduledTime(), body.type(), body.emergencyId()));
  }

  @PostMapping("/me/appointments/{id}/confirm")
  ResponseEntity<Appointment> confirm(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
    return ResponseEntity.ok(appointments.confirm(userId, id));
  }

  @PostMapping("/me/appointments/{id}/reschedule")
  ResponseEntity<Appointment> reschedule(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(
        appointments.reschedule(userId, id, Instant.parse(body.get("scheduledTime"))));
  }

  @PostMapping("/me/appointments/{id}/cancel")
  ResponseEntity<Appointment> cancelAppt(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long id,
      @RequestBody(required = false) Map<String, String> body) {
    return ResponseEntity.ok(appointments.cancel(userId, id, body != null ? body.get("reason") : null));
  }

  @PostMapping("/me/appointments/{id}/check-in")
  ResponseEntity<Appointment> checkIn(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(
        appointments.checkInDonor(userId, id, body.getOrDefault("mode", "QR")));
  }

  @GetMapping("/me/appointments/history")
  ResponseEntity<List<Appointment>> apptHistory(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(appointments.historyForDonor(userId));
  }

  @GetMapping("/me/emergencies/{emergencyId}")
  ResponseEntity<Emergency> emergencyDetail(
      @RequestHeader("X-User-Id") Long userId, @PathVariable Long emergencyId) {
    donorProfile.requireForUser(userId);
    return ResponseEntity.ok(emergencies.requireEmergency(emergencyId));
  }

  @GetMapping("/me/emergencies/{emergencyId}/responses/aggregate")
  ResponseEntity<Map<String, Long>> emergencyAgg(@PathVariable Long emergencyId) {
    return ResponseEntity.ok(emergencies.aggregateResponseCounts(emergencyId));
  }

  @PostMapping("/me/emergencies/{emergencyId}/accept")
  ResponseEntity<EmergencyResponse> accept(
      @RequestHeader("X-User-Id") Long userId, @PathVariable Long emergencyId) {
    return ResponseEntity.ok(emergencies.accept(userId, emergencyId));
  }

  @PostMapping("/me/emergencies/{emergencyId}/decline")
  ResponseEntity<EmergencyResponse> decline(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long emergencyId,
      @RequestBody(required = false) Map<String, String> body) {
    return ResponseEntity.ok(
        emergencies.decline(userId, emergencyId, body != null ? body.get("reason") : null));
  }

  @GetMapping("/me/emergency-history")
  ResponseEntity<List<EmergencyResponse>> emergencyHistory(
      @RequestHeader("X-User-Id") Long userId) {
    donorProfile.requireForUser(userId);
    return ResponseEntity.ok(emergencies.listResponsesForDonorUser(userId));
  }

  @GetMapping("/me/notifications")
  ResponseEntity<List<Notification>> notifs(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(userNotifications.listRecentForUser(userId));
  }

  @GetMapping("/me/donations/history")
  ResponseEntity<List<Appointment>> donations(@RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(donationInsights.completedDonationsForDonor(userId));
  }
}
