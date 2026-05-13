package com.qatra.donationplatform.appointment.core.domain.port.incoming;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.shared.domain.enums.AppointmentType;
import java.time.Instant;
import java.util.List;


public interface AppointmentSchedulingPort {

  Appointment schedule(
      Long donorUserId,
      Long centerId,
      Instant scheduledTime,
      AppointmentType type,
      Long emergencyId);

  Appointment confirm(Long donorUserId, Long appointmentId);

  Appointment reschedule(Long donorUserId, Long appointmentId, Instant newTime);

  Appointment cancel(Long donorUserId, Long appointmentId, String reason);

  Appointment checkInDonor(Long donorUserId, Long appointmentId, String mode);

  Appointment completeByStaff(Long staffUserId, Long appointmentId, int mlCollected, String notes);

  Appointment markNoShow(Long staffUserId, Long appointmentId);

  Appointment walkIn(Long staffUserId, Long donorProfileId, Long centerId);

  List<Appointment> historyForDonor(Long donorUserId);

  List<Appointment> centerSchedule(Long centerId, Instant from, Instant to);

  void sendUpcomingReminders(Instant now);

  void releaseUnconfirmedSlots(Instant now);
}
