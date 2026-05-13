package com.qatra.donationplatform.emergency.core.domain.service;

import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import com.qatra.donationplatform.emergency.core.domain.port.incoming.EmergencyAppointmentCallbackPort;
import com.qatra.donationplatform.emergency.core.domain.port.outgoing.EmergencyResponseRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.ResponseType;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmergencyAppointmentCallbackService implements EmergencyAppointmentCallbackPort {

  private final EmergencyResponseRepositoryPort responses;

  public EmergencyAppointmentCallbackService(EmergencyResponseRepositoryPort responses) {
    this.responses = responses;
  }

  @Override
  @Transactional
  public void onEmergencyAppointment(Long emergencyId, Long donorProfileId) {
    EmergencyResponse r =
        responses
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
    r.setResponseType(ResponseType.CONVERTED_TO_APPOINTMENT);
    r.setRespondedAt(Instant.now());
    responses.save(r);
  }
}
