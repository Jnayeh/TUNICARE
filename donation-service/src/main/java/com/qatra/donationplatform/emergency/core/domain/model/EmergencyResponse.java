package com.qatra.donationplatform.emergency.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.ResponseType;
import java.time.Instant;


/** Class diagram: EmergencyResponse */
public class EmergencyResponse {

  private Long id;
  private Long emergencyId;
  private Long donorProfileId;
  private ResponseType responseType;
  private String message;
  private Instant respondedAt;
  private Instant notifiedAt;

  public void respond(ResponseType type, String message, Instant when) {
    this.responseType = type;
    this.message = message;
    this.respondedAt = when;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEmergencyId() {
    return emergencyId;
  }

  public void setEmergencyId(Long emergencyId) {
    this.emergencyId = emergencyId;
  }

  public Long getDonorProfileId() {
    return donorProfileId;
  }

  public void setDonorProfileId(Long donorProfileId) {
    this.donorProfileId = donorProfileId;
  }

  public ResponseType getResponseType() {
    return responseType;
  }

  public void setResponseType(ResponseType responseType) {
    this.responseType = responseType;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Instant getRespondedAt() {
    return respondedAt;
  }

  public void setRespondedAt(Instant respondedAt) {
    this.respondedAt = respondedAt;
  }

  public Instant getNotifiedAt() {
    return notifiedAt;
  }

  public void setNotifiedAt(Instant notifiedAt) {
    this.notifiedAt = notifiedAt;
  }
}
