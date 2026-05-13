package com.qatra.donationplatform.emergency.core.domain.port.incoming;

import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.EmergencyUrgency;
import java.time.Instant;
import java.util.List;
import java.util.Map;


public interface EmergencyOperationsPort {

  Emergency requireEmergency(Long id);

  Emergency create(
      Long staffUserId,
      Long centerId,
      BloodType bloodTypeNeeded,
      int unitsNeeded,
      EmergencyUrgency urgency,
      Instant neededBy,
      Integer matchRadiusKm,
      String contactPerson,
      String contactPhone,
      String patientInfo);

  Emergency escalateUrgency(Long staffUserId, Long emergencyId, EmergencyUrgency newUrgency);

  Emergency extendDeadline(Long staffUserId, Long emergencyId, Instant neededBy);

  Emergency cancel(Long staffUserId, Long emergencyId);

  Emergency resolve(Long staffUserId, Long emergencyId);

  EmergencyResponse accept(Long donorUserId, Long emergencyId);

  EmergencyResponse decline(Long donorUserId, Long emergencyId, String reason);

  Map<String, Long> aggregateResponseCounts(Long emergencyId);

  List<EmergencyResponse> listResponsesForDonorUser(Long donorUserId);

  List<Emergency> listEmergenciesForStaffUser(Long staffUserId);
}
