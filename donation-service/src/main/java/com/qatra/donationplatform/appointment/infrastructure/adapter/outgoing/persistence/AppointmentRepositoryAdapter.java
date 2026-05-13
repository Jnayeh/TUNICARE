package com.qatra.donationplatform.appointment.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.appointment.core.domain.port.outgoing.AppointmentRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AppointmentRepositoryAdapter implements AppointmentRepositoryPort {

  private final AppointmentJpaRepository jpa;
  private final AppointmentPersistenceMapper mapper;

  public AppointmentRepositoryAdapter(AppointmentJpaRepository jpa, AppointmentPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<Appointment> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Appointment> findByDonorProfileIdOrderByScheduledTimeDesc(Long donorProfileId) {
    return jpa.findByDonorProfileIdOrderByScheduledTimeDesc(donorProfileId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Appointment> findByCenterIdAndScheduledTimeBetween(
      Long centerId, Instant start, Instant end) {
    return jpa.findByCenterIdAndScheduledTimeBetweenOrderByScheduledTime(centerId, start, end).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Appointment> findByEmergencyId(Long emergencyId) {
    return jpa.findByEmergencyId(emergencyId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public List<Appointment> findByStatusAndConfirmedAtIsNullAndCreatedAtBefore(
      AppointmentStatus status, Instant cutoff) {
    return jpa.findByStatusAndConfirmedAtIsNullAndCreatedAtBefore(status, cutoff).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Appointment save(Appointment appointment) {
    return mapper.toDomain(jpa.save(mapper.toEntity(appointment)));
  }

  @Override
  public List<Appointment> findAllForReminderWindow(Instant from, Instant to) {
    return jpa.findReminderWindow(from, to).stream().map(mapper::toDomain).collect(Collectors.toList());
  }
}
