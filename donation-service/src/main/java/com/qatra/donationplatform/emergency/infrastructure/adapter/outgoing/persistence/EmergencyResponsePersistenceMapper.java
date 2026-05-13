package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import org.springframework.stereotype.Component;

@Component
public class EmergencyResponsePersistenceMapper {

  public EmergencyResponse toDomain(EmergencyResponseEntity e) {
    EmergencyResponse r = new EmergencyResponse();
    r.setId(e.getId());
    r.setEmergencyId(e.getEmergencyId());
    r.setDonorProfileId(e.getDonorProfileId());
    r.setResponseType(e.getResponseType());
    r.setMessage(e.getMessage());
    r.setRespondedAt(e.getRespondedAt());
    r.setNotifiedAt(e.getNotifiedAt());
    return r;
  }

  public EmergencyResponseEntity toEntity(EmergencyResponse r) {
    EmergencyResponseEntity e = new EmergencyResponseEntity();
    e.setId(r.getId());
    e.setEmergencyId(r.getEmergencyId());
    e.setDonorProfileId(r.getDonorProfileId());
    e.setResponseType(r.getResponseType());
    e.setMessage(r.getMessage());
    e.setRespondedAt(r.getRespondedAt());
    e.setNotifiedAt(r.getNotifiedAt());
    return e;
  }
}
