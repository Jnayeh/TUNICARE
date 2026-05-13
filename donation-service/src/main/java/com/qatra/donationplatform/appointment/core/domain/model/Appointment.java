package com.qatra.donationplatform.appointment.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import com.qatra.donationplatform.shared.domain.enums.AppointmentType;
import java.time.Instant;


/** Class diagram: Appointment */
public class Appointment {

  private Long id;
  private Long donorProfileId;
  private Long centerId;
  private Long emergencyId;
  private Instant scheduledTime;
  private int estimatedDuration = 60;
  private AppointmentStatus status;
  private AppointmentType appointmentType;
  private Integer mlCollected;
  private String notes;
  private String cancellationReason;
  private Long completedByStaffProfileId;
  private Instant createdAt;
  private Instant confirmedAt;
  private Instant completedAt;
  private Instant cancelledAt;

  public void confirm(Instant now) {
    this.status = AppointmentStatus.CONFIRMED;
    this.confirmedAt = now;
  }

  public void complete(int mlCollected, String notes, Long completedByStaffProfileId, Instant now) {
    this.status = AppointmentStatus.COMPLETED;
    this.mlCollected = mlCollected;
    this.notes = notes;
    this.completedByStaffProfileId = completedByStaffProfileId;
    this.completedAt = now;
  }

  public void cancel(String reason, Instant now) {
    this.status = AppointmentStatus.CANCELLED;
    this.cancellationReason = reason;
    this.cancelledAt = now;
  }

  public void markNoShow() {
    this.status = AppointmentStatus.NO_SHOW;
  }

  public void reschedule(Instant newScheduledTime) {
    this.scheduledTime = newScheduledTime;
    this.status = AppointmentStatus.RESCHEDULED;
  }

  public void checkIn(Instant now) {
    this.status = AppointmentStatus.IN_PROGRESS;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDonorProfileId() {
    return donorProfileId;
  }

  public void setDonorProfileId(Long donorProfileId) {
    this.donorProfileId = donorProfileId;
  }

  public Long getCenterId() {
    return centerId;
  }

  public void setCenterId(Long centerId) {
    this.centerId = centerId;
  }

  public Long getEmergencyId() {
    return emergencyId;
  }

  public void setEmergencyId(Long emergencyId) {
    this.emergencyId = emergencyId;
  }

  public Instant getScheduledTime() {
    return scheduledTime;
  }

  public void setScheduledTime(Instant scheduledTime) {
    this.scheduledTime = scheduledTime;
  }

  public int getEstimatedDuration() {
    return estimatedDuration;
  }

  public void setEstimatedDuration(int estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public AppointmentType getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(AppointmentType appointmentType) {
    this.appointmentType = appointmentType;
  }

  public Integer getMlCollected() {
    return mlCollected;
  }

  public void setMlCollected(Integer mlCollected) {
    this.mlCollected = mlCollected;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public Long getCompletedByStaffProfileId() {
    return completedByStaffProfileId;
  }

  public void setCompletedByStaffProfileId(Long completedByStaffProfileId) {
    this.completedByStaffProfileId = completedByStaffProfileId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getConfirmedAt() {
    return confirmedAt;
  }

  public void setConfirmedAt(Instant confirmedAt) {
    this.confirmedAt = confirmedAt;
  }

  public Instant getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(Instant completedAt) {
    this.completedAt = completedAt;
  }

  public Instant getCancelledAt() {
    return cancelledAt;
  }

  public void setCancelledAt(Instant cancelledAt) {
    this.cancelledAt = cancelledAt;
  }
}
