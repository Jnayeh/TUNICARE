package com.qatra.donationplatform.emergency.core.domain.port.outgoing;

import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import java.util.List;
import java.util.Optional;


public interface EmergencyResponseRepositoryPort {

  Optional<EmergencyResponse> findByEmergencyIdAndDonorProfileId(Long emergencyId, Long donorProfileId);

  List<EmergencyResponse> findByEmergencyId(Long emergencyId);

  List<EmergencyResponse> findByDonorProfileId(Long donorProfileId);

  EmergencyResponse save(EmergencyResponse response);

  long countByEmergencyId(Long emergencyId);
}
