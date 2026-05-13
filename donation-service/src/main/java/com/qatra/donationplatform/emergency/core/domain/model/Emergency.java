package com.qatra.donationplatform.emergency.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import com.qatra.donationplatform.shared.domain.enums.EmergencyUrgency;
import java.time.Instant;


/** Class diagram: Emergency */
public class Emergency {

  private Long id;
  private Long centerId;
  private Long createdByStaffProfileId;
  private BloodType bloodTypeNeeded;
  private int unitsNeeded;
  private int unitsCollected;
  private EmergencyUrgency urgency;
  private String contactPerson;
  private String contactPhone;
  private String patientInfo;
  private EmergencyStatus status;
  private Integer matchRadius;
  private Instant neededBy;
  private Instant createdAt;
  private Instant resolvedAt;
  private Long resolvedByStaffProfileId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCenterId() {
    return centerId;
  }

  public void setCenterId(Long centerId) {
    this.centerId = centerId;
  }

  public Long getCreatedByStaffProfileId() {
    return createdByStaffProfileId;
  }

  public void setCreatedByStaffProfileId(Long createdByStaffProfileId) {
    this.createdByStaffProfileId = createdByStaffProfileId;
  }

  public BloodType getBloodTypeNeeded() {
    return bloodTypeNeeded;
  }

  public void setBloodTypeNeeded(BloodType bloodTypeNeeded) {
    this.bloodTypeNeeded = bloodTypeNeeded;
  }

  public int getUnitsNeeded() {
    return unitsNeeded;
  }

  public void setUnitsNeeded(int unitsNeeded) {
    this.unitsNeeded = unitsNeeded;
  }

  public int getUnitsCollected() {
    return unitsCollected;
  }

  public void setUnitsCollected(int unitsCollected) {
    this.unitsCollected = unitsCollected;
  }

  public EmergencyUrgency getUrgency() {
    return urgency;
  }

  public void setUrgency(EmergencyUrgency urgency) {
    this.urgency = urgency;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public String getPatientInfo() {
    return patientInfo;
  }

  public void setPatientInfo(String patientInfo) {
    this.patientInfo = patientInfo;
  }

  public EmergencyStatus getStatus() {
    return status;
  }

  public void setStatus(EmergencyStatus status) {
    this.status = status;
  }

  public Integer getMatchRadius() {
    return matchRadius;
  }

  public void setMatchRadius(Integer matchRadius) {
    this.matchRadius = matchRadius;
  }

  public Instant getNeededBy() {
    return neededBy;
  }

  public void setNeededBy(Instant neededBy) {
    this.neededBy = neededBy;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(Instant resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  public Long getResolvedByStaffProfileId() {
    return resolvedByStaffProfileId;
  }

  public void setResolvedByStaffProfileId(Long resolvedByStaffProfileId) {
    this.resolvedByStaffProfileId = resolvedByStaffProfileId;
  }
}
