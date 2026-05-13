package com.qatra.donationplatform.appointment.core.domain.service;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.appointment.core.domain.port.incoming.AppointmentSchedulingPort;
import com.qatra.donationplatform.appointment.core.domain.port.outgoing.AppointmentRepositoryPort;
import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.outgoing.BloodDonationCenterRepositoryPort;
import com.qatra.donationplatform.emergency.core.domain.port.incoming.EmergencyAppointmentCallbackPort;
import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.DonorProfilePort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.AuditJournalPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.NotificationDispatchPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.SystemConfigurationPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterStaffProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import com.qatra.donationplatform.shared.domain.enums.AppointmentType;
import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import com.qatra.donationplatform.shared.exception.ConflictException;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentManagementService implements AppointmentSchedulingPort {

  private final AppointmentRepositoryPort appointments;
  private final DonorProfileRepositoryPort donorProfiles;
  private final DonorProfilePort donorProfilePort;
  private final BloodDonationCenterRepositoryPort centers;
  private final SystemConfigurationPort config;
  private final NotificationDispatchPort notifications;
  private final AuditJournalPort audit;
  private final CenterStaffProfileRepositoryPort staffProfiles;
  private final EmergencyAppointmentCallbackPort emergencyAppointmentCallback;

  public AppointmentManagementService(
      AppointmentRepositoryPort appointments,
      DonorProfileRepositoryPort donorProfiles,
      DonorProfilePort donorProfilePort,
      BloodDonationCenterRepositoryPort centers,
      SystemConfigurationPort config,
      NotificationDispatchPort notifications,
      AuditJournalPort audit,
      CenterStaffProfileRepositoryPort staffProfiles,
      EmergencyAppointmentCallbackPort emergencyAppointmentCallback) {
    this.appointments = appointments;
    this.donorProfiles = donorProfiles;
    this.donorProfilePort = donorProfilePort;
    this.centers = centers;
    this.config = config;
    this.notifications = notifications;
    this.audit = audit;
    this.staffProfiles = staffProfiles;
    this.emergencyAppointmentCallback = emergencyAppointmentCallback;
  }

  @Override
  @Transactional
  public Appointment schedule(
      Long donorUserId,
      Long centerId,
      Instant scheduledTime,
      AppointmentType type,
      Long emergencyId) {
    DonorProfile donor =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    assertCapacity(centerId, scheduledTime);
    Appointment a = new Appointment();
    a.setDonorProfileId(donor.getId());
    a.setCenterId(centerId);
    a.setEmergencyId(emergencyId);
    a.setScheduledTime(scheduledTime);
    a.setAppointmentType(type);
    a.setStatus(AppointmentStatus.SCHEDULED);
    a = appointments.save(a);
    if (emergencyId != null) {
      emergencyAppointmentCallback.onEmergencyAppointment(emergencyId, donor.getId());
    }
    notifications.dispatch(
        donorUserId,
        NotificationType.APPOINTMENT_REMINDER,
        NotificationChannel.IN_APP,
        "Appointment scheduled",
        "Your donation appointment is booked.",
        Map.of("appointmentId", a.getId().toString()));
    return a;
  }

  @Override
  @Transactional
  public Appointment confirm(Long donorUserId, Long appointmentId) {
    Appointment a = forDonor(donorUserId, appointmentId);
    a.confirm(Instant.now());
    return appointments.save(a);
  }

  @Override
  @Transactional
  public Appointment reschedule(Long donorUserId, Long appointmentId, Instant newTime) {
    Appointment a = forDonor(donorUserId, appointmentId);
    assertCapacity(a.getCenterId(), newTime);
    a.reschedule(newTime);
    return appointments.save(a);
  }

  @Override
  @Transactional
  public Appointment cancel(Long donorUserId, Long appointmentId, String reason) {
    Appointment a = forDonor(donorUserId, appointmentId);
    a.cancel(reason, Instant.now());
    return appointments.save(a);
  }

  @Override
  @Transactional
  public Appointment checkInDonor(Long donorUserId, Long appointmentId, String mode) {
    Appointment a = forDonor(donorUserId, appointmentId);
    a.checkIn(Instant.now());
    appointments.save(a);
    audit.record(
        donorUserId,
        "APPOINTMENT_CHECKIN",
        "Appointment",
        a.getId(),
        null,
        Map.of("mode", mode),
        null,
        null);
    return a;
  }

  @Override
  @Transactional
  public Appointment completeByStaff(
      Long staffUserId, Long appointmentId, int mlCollected, String notes) {
    var staff = staffProfiles.findByUserId(staffUserId).orElseThrow();
    Appointment a =
        appointments.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));
    a.complete(mlCollected, notes, staff.getId(), Instant.now());
    appointments.save(a);
    int cooldown = config.getCooldownDays();
    donorProfilePort.applyDonationRecorded(a.getDonorProfileId(), mlCollected, cooldown);
    Long donorUserId =
        donorProfiles
            .findById(a.getDonorProfileId())
            .orElseThrow()
            .getUserId();
    notifications.dispatch(
        donorUserId,
        NotificationType.THANK_YOU_MESSAGE,
        NotificationChannel.IN_APP,
        "Thank you",
        "Your donation was recorded.",
        Map.of("appointmentId", appointmentId.toString()));
    audit.record(
        staffUserId,
        "APPOINTMENT_COMPLETE",
        "Appointment",
        a.getId(),
        null,
        Map.of("ml", mlCollected),
        null,
        null);
    return a;
  }

  @Override
  @Transactional
  public Appointment markNoShow(Long staffUserId, Long appointmentId) {
    Appointment a =
        appointments.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));
    a.markNoShow();
    appointments.save(a);
    donorProfilePort.applyReliabilityDelta(a.getDonorProfileId(), -12d);
    audit.record(
        staffUserId,
        "APPOINTMENT_NO_SHOW",
        "Appointment",
        a.getId(),
        null,
        Map.of(),
        null,
        null);
    return a;
  }

  @Override
  @Transactional
  public Appointment walkIn(Long staffUserId, Long donorProfileId, Long centerId) {
    staffProfiles.findByUserId(staffUserId).orElseThrow();
    Appointment a = new Appointment();
    a.setDonorProfileId(donorProfileId);
    a.setCenterId(centerId);
    a.setScheduledTime(Instant.now());
    a.setAppointmentType(AppointmentType.WALK_IN);
    a.setStatus(AppointmentStatus.IN_PROGRESS);
    return appointments.save(a);
  }

  @Override
  public List<Appointment> historyForDonor(Long donorUserId) {
    DonorProfile d =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    return appointments.findByDonorProfileIdOrderByScheduledTimeDesc(d.getId());
  }

  @Override
  public List<Appointment> centerSchedule(Long centerId, Instant from, Instant to) {
    return appointments.findByCenterIdAndScheduledTimeBetween(centerId, from, to);
  }

  @Override
  @Transactional
  public void sendUpcomingReminders(Instant now) {
    Instant to = now.plus(24, ChronoUnit.HOURS);
    for (Appointment a : appointments.findAllForReminderWindow(now, to)) {
      if (a.getStatus() != AppointmentStatus.CONFIRMED
          && a.getStatus() != AppointmentStatus.SCHEDULED) {
        continue;
      }
      Long donorUserId =
          donorProfiles
              .findById(a.getDonorProfileId())
              .orElseThrow()
              .getUserId();
      notifications.dispatch(
          donorUserId,
          NotificationType.APPOINTMENT_REMINDER,
          NotificationChannel.IN_APP,
          "Reminder: donation appointment",
          "You have an appointment within 24 hours.",
          Map.of("appointmentId", a.getId().toString()));
    }
  }

  @Override
  @Transactional
  public void releaseUnconfirmedSlots(Instant now) {
    Instant cutoff = now.minus(config.getAppointmentConfirmTimeoutHours(), ChronoUnit.HOURS);
    for (Appointment a :
        appointments.findByStatusAndConfirmedAtIsNullAndCreatedAtBefore(
            AppointmentStatus.SCHEDULED, cutoff)) {
      a.cancel("Auto-released: not confirmed in time", now);
      appointments.save(a);
    }
  }

  private void assertCapacity(Long centerId, Instant scheduledTime) {
    BloodDonationCenter c =
        centers.findById(centerId).orElseThrow(() -> new NotFoundException("Center not found"));
    Instant start = scheduledTime.truncatedTo(ChronoUnit.DAYS);
    Instant end = start.plus(1, ChronoUnit.DAYS);
    long count =
        appointments.findByCenterIdAndScheduledTimeBetween(centerId, start, end).stream()
            .filter(
                x ->
                    x.getStatus() != AppointmentStatus.CANCELLED
                        && x.getStatus() != AppointmentStatus.NO_SHOW)
            .count();
    if (c.getDailyCapacity() > 0 && count >= c.getDailyCapacity()) {
      throw new ConflictException("Center capacity reached for day");
    }
  }

  private Appointment forDonor(Long donorUserId, Long appointmentId) {
    DonorProfile d =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    Appointment a =
        appointments.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));
    if (!a.getDonorProfileId().equals(d.getId())) {
      throw new IllegalArgumentException("Not donor appointment");
    }
    return a;
  }
}
