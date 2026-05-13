package com.qatra.donationplatform.appointment.core.domain.port.outgoing;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface AppointmentRepositoryPort {

  Optional<Appointment> findById(Long id);

  List<Appointment> findByDonorProfileIdOrderByScheduledTimeDesc(Long donorProfileId);

  List<Appointment> findByCenterIdAndScheduledTimeBetween(Long centerId, Instant start, Instant end);

  List<Appointment> findByEmergencyId(Long emergencyId);

  List<Appointment> findByStatusAndConfirmedAtIsNullAndCreatedAtBefore(
      AppointmentStatus status, Instant cutoff);

  Appointment save(Appointment appointment);

  List<Appointment> findAllForReminderWindow(Instant from, Instant to);
}
