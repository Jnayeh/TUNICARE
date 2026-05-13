package com.qatra.donationplatform.appointment.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentPersistenceMapper {

  public Appointment toDomain(AppointmentEntity e) {
    Appointment a = new Appointment();
    a.setId(e.getId());
    a.setDonorProfileId(e.getDonorProfileId());
    a.setCenterId(e.getCenterId());
    a.setEmergencyId(e.getEmergencyId());
    a.setScheduledTime(e.getScheduledTime());
    a.setEstimatedDuration(e.getEstimatedDuration());
    a.setStatus(e.getStatus());
    a.setAppointmentType(e.getAppointmentType());
    a.setMlCollected(e.getMlCollected());
    a.setNotes(e.getNotes());
    a.setCancellationReason(e.getCancellationReason());
    a.setCompletedByStaffProfileId(e.getCompletedByStaffProfileId());
    a.setCreatedAt(e.getCreatedAt());
    a.setConfirmedAt(e.getConfirmedAt());
    a.setCompletedAt(e.getCompletedAt());
    a.setCancelledAt(e.getCancelledAt());
    return a;
  }

  public AppointmentEntity toEntity(Appointment a) {
    AppointmentEntity e = new AppointmentEntity();
    e.setId(a.getId());
    e.setDonorProfileId(a.getDonorProfileId());
    e.setCenterId(a.getCenterId());
    e.setEmergencyId(a.getEmergencyId());
    e.setScheduledTime(a.getScheduledTime());
    e.setEstimatedDuration(a.getEstimatedDuration());
    e.setStatus(a.getStatus());
    e.setAppointmentType(a.getAppointmentType());
    e.setMlCollected(a.getMlCollected());
    e.setNotes(a.getNotes());
    e.setCancellationReason(a.getCancellationReason());
    e.setCompletedByStaffProfileId(a.getCompletedByStaffProfileId());
    e.setCreatedAt(a.getCreatedAt());
    e.setConfirmedAt(a.getConfirmedAt());
    e.setCompletedAt(a.getCompletedAt());
    e.setCancelledAt(a.getCancelledAt());
    return e;
  }
}
