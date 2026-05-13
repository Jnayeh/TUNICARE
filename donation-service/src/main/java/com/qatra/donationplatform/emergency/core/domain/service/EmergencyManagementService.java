package com.qatra.donationplatform.emergency.core.domain.service;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.outgoing.BloodDonationCenterRepositoryPort;
import com.qatra.donationplatform.emergency.core.domain.BloodCompatibility;
import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import com.qatra.donationplatform.emergency.core.domain.port.incoming.EmergencyOperationsPort;
import com.qatra.donationplatform.emergency.core.domain.port.outgoing.EmergencyRepositoryPort;
import com.qatra.donationplatform.emergency.core.domain.port.outgoing.EmergencyResponseRepositoryPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.AuditJournalPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.NotificationDispatchPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.SystemConfigurationPort;
import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.DonorProfilePort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterStaffProfileRepositoryPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import com.qatra.donationplatform.shared.domain.enums.EmergencyUrgency;
import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import com.qatra.donationplatform.shared.domain.enums.ResponseType;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import com.qatra.donationplatform.shared.util.GeoUtils;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmergencyManagementService implements EmergencyOperationsPort {

  private final EmergencyRepositoryPort emergencies;
  private final EmergencyResponseRepositoryPort responses;
  private final DonorProfileRepositoryPort donorProfiles;
  private final BloodDonationCenterRepositoryPort centers;
  private final CenterStaffProfileRepositoryPort staff;
  private final NotificationDispatchPort notifications;
  private final SystemConfigurationPort config;
  private final AuditJournalPort audit;
  private final DonorProfilePort donorProfilePort;

  record MatchedDonor(DonorProfile donor, double distanceKm, int tier, double score) {}

  @Override
  public Emergency requireEmergency(Long id) {
    return emergencies.findById(id).orElseThrow(() -> new NotFoundException("Emergency not found"));
  }

  public EmergencyManagementService(
      EmergencyRepositoryPort emergencies,
      EmergencyResponseRepositoryPort responses,
      DonorProfileRepositoryPort donorProfiles,
      BloodDonationCenterRepositoryPort centers,
      CenterStaffProfileRepositoryPort staff,
      NotificationDispatchPort notifications,
      SystemConfigurationPort config,
      AuditJournalPort audit,
      DonorProfilePort donorProfilePort) {
    this.emergencies = emergencies;
    this.responses = responses;
    this.donorProfiles = donorProfiles;
    this.centers = centers;
    this.staff = staff;
    this.notifications = notifications;
    this.config = config;
    this.audit = audit;
    this.donorProfilePort = donorProfilePort;
  }

  @Override
  @Transactional
  public Emergency create(
      Long staffUserId,
      Long centerId,
      BloodType bloodTypeNeeded,
      int unitsNeeded,
      EmergencyUrgency urgency,
      Instant neededBy,
      Integer matchRadiusKm,
      String contactPerson,
      String contactPhone,
      String patientInfo) {
    var staffProfile = staff.findByUserId(staffUserId).orElseThrow();
    Long resolvedCenter = centerId != null ? centerId : staffProfile.getCenterId();
    Emergency e = new Emergency();
    e.setCenterId(resolvedCenter);
    e.setCreatedByStaffProfileId(staffProfile.getId());
    e.setBloodTypeNeeded(bloodTypeNeeded);
    e.setUnitsNeeded(unitsNeeded);
    e.setUrgency(urgency);
    e.setNeededBy(neededBy);
    e.setContactPerson(contactPerson);
    e.setContactPhone(contactPhone);
    e.setPatientInfo(patientInfo);
    e.setStatus(EmergencyStatus.NOTIFYING);
    e.setMatchRadius(matchRadiusKm != null ? matchRadiusKm : config.getDefaultMatchRadiusKm());
    e = emergencies.save(e);
    matchAndNotify(e, staffUserId);
    audit.record(
        staffUserId,
        "EMERGENCY_CREATED",
        "Emergency",
        e.getId(),
        null,
        Map.of("centerId", resolvedCenter.toString()),
        null,
        null);
    return e;
  }

  @Override
  @Transactional
  public Emergency escalateUrgency(Long staffUserId, Long emergencyId, EmergencyUrgency newUrgency) {
    Emergency e = emergencies.findById(emergencyId).orElseThrow();
    e.setUrgency(newUrgency);
    return emergencies.save(e);
  }

  @Override
  @Transactional
  public Emergency extendDeadline(Long staffUserId, Long emergencyId, Instant neededBy) {
    Emergency e = emergencies.findById(emergencyId).orElseThrow();
    e.setNeededBy(neededBy);
    return emergencies.save(e);
  }

  @Override
  @Transactional
  public Emergency cancel(Long staffUserId, Long emergencyId) {
    Emergency e = emergencies.findById(emergencyId).orElseThrow();
    e.setStatus(EmergencyStatus.CANCELLED);
    return emergencies.save(e);
  }

  @Override
  @Transactional
  public Emergency resolve(Long staffUserId, Long emergencyId) {
    var st = staff.findByUserId(staffUserId).orElseThrow();
    Emergency e = emergencies.findById(emergencyId).orElseThrow();
    e.setStatus(EmergencyStatus.RESOLVED);
    e.setResolvedAt(Instant.now());
    e.setResolvedByStaffProfileId(st.getId());
    return emergencies.save(e);
  }

  @Override
  @Transactional
  public EmergencyResponse accept(Long donorUserId, Long emergencyId) {
    var donor = donorProfiles.findByUserId(donorUserId).orElseThrow();
    EmergencyResponse r = responseOrCreate(emergencyId, donor.getId());
    r.respond(ResponseType.WILLING, null, Instant.now());
    return responses.save(r);
  }

  @Override
  @Transactional
  public EmergencyResponse decline(Long donorUserId, Long emergencyId, String reason) {
    var donor = donorProfiles.findByUserId(donorUserId).orElseThrow();
    EmergencyResponse r = responseOrCreate(emergencyId, donor.getId());
    r.respond(ResponseType.DECLINED, reason, Instant.now());
    donorProfilePort.applyReliabilityDelta(donor.getId(), -4d);
    return responses.save(r);
  }

  @Override
  public Map<String, Long> aggregateResponseCounts(Long emergencyId) {
    List<EmergencyResponse> all = responses.findByEmergencyId(emergencyId);
    long willing = all.stream().filter(x -> x.getResponseType() == ResponseType.WILLING).count();
    long declined = all.stream().filter(x -> x.getResponseType() == ResponseType.DECLINED).count();
    long pending = all.stream().filter(x -> x.getResponseType() == ResponseType.NO_RESPONSE).count();
    return Map.of("willingCount", willing, "declinedCount", declined, "noResponseCount", pending);
  }

  @Override
  public List<EmergencyResponse> listResponsesForDonorUser(Long donorUserId) {
    var donor =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    return responses.findByDonorProfileId(donor.getId());
  }

  @Override
  public List<Emergency> listEmergenciesForStaffUser(Long staffUserId) {
    var st =
        staff
            .findByUserId(staffUserId)
            .orElseThrow(() -> new NotFoundException("Staff profile not found"));
    return emergencies.findByCenterIdOrderByCreatedAtDesc(st.getCenterId());
  }

  private EmergencyResponse responseOrCreate(Long emergencyId, Long donorProfileId) {
    return responses
        .findByEmergencyIdAndDonorProfileId(emergencyId, donorProfileId)
        .orElseGet(
            () -> {
              EmergencyResponse n = new EmergencyResponse();
              n.setEmergencyId(emergencyId);
              n.setDonorProfileId(donorProfileId);
              n.setResponseType(ResponseType.NO_RESPONSE);
              n.setNotifiedAt(Instant.now());
              return responses.save(n);
            });
  }

  private void matchAndNotify(Emergency emergency, Long staffUserId) {
    BloodDonationCenter center =
        centers.findById(emergency.getCenterId()).orElseThrow();
    double radiusKm = emergency.getMatchRadius();
    List<MatchedDonor> tiered = new ArrayList<>();
    attemptMatch(emergency, center, radiusKm, tiered);
    int attempts = 0;
    while (tiered.size() < emergency.getUnitsNeeded() * 3 && attempts < 3 && radiusKm < 500) {
      radiusKm *= 2;
      emergency.setMatchRadius((int) Math.round(radiusKm));
      emergencies.save(emergency);
      tiered.clear();
      attemptMatch(emergency, center, radiusKm, tiered);
      attempts++;
    }
    tiered.sort(
        Comparator.comparingInt(MatchedDonor::tier)
            .thenComparing(Comparator.comparingDouble(MatchedDonor::score).reversed()));
    int cap = Math.min(50, tiered.size());
    for (int i = 0; i < cap; i++) {
      MatchedDonor m = tiered.get(i);
      DonorProfile d = m.donor();
      EmergencyResponse row = new EmergencyResponse();
      row.setEmergencyId(emergency.getId());
      row.setDonorProfileId(d.getId());
      row.setResponseType(ResponseType.NO_RESPONSE);
      row.setNotifiedAt(Instant.now());
      responses.save(row);
      notifications.dispatch(
          d.getUserId(),
          NotificationType.EMERGENCY_ALERT,
          preferredChannel(d),
          "Emergency blood need near you",
          "A center needs donors compatible with "
              + emergency.getBloodTypeNeeded()
              + ". Distance ~"
              + String.format("%.1f km", m.distanceKm()),
          Map.of(
              "emergencyId",
              emergency.getId().toString(),
              "centerId",
              emergency.getCenterId().toString(),
              "urgency",
              emergency.getUrgency().name(),
              "bloodType",
              emergency.getBloodTypeNeeded().name(),
              "distanceKm",
              m.distanceKm()));
    }
  }

  private NotificationChannel preferredChannel(DonorProfile d) {
    return switch (d.getNotificationFrequency()) {
      case DISABLED -> NotificationChannel.IN_APP;
      case DAILY_DIGEST, EMERGENCY_ONLY, IMMEDIATE -> NotificationChannel.PUSH;
    };
  }

  private void attemptMatch(
      Emergency emergency, BloodDonationCenter center, double radiusKm, List<MatchedDonor> out) {
    Set<BloodType> compatible = BloodCompatibility.compatibleDonorTypes(emergency.getBloodTypeNeeded());
    List<DonorProfile> donors = donorProfiles.findByAvailability(AvailabilityStatus.AVAILABLE);
    LocalDate today = LocalDate.now();
    for (DonorProfile d : donors) {
      if (!d.isAllowEmergencyNotifications()) {
        continue;
      }
      if (d.isPermanentRestriction()) {
        continue;
      }
      if (d.getEligibleFromDate() != null && d.getEligibleFromDate().isAfter(today)) {
        continue;
      }
      BloodType bt = d.getBloodType();
      if (bt != null && bt != BloodType.UNKNOWN && !compatible.contains(bt)) {
        continue;
      }
      double dist =
          GeoUtils.distanceKm(
              center.getLatitude(), center.getLongitude(), d.getLatitude(), d.getLongitude());
      if (dist > radiusKm) {
        continue;
      }
      int tier =
          bt == emergency.getBloodTypeNeeded()
              ? 1
              : (bt == null || bt == BloodType.UNKNOWN ? 3 : 2);
      double score = d.getReliabilityScore() - dist * 0.15 + tier * 8d;
      out.add(new MatchedDonor(d, dist, tier, score));
    }
  }
}
