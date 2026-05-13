package com.qatra.donationplatform.emergency.core.domain.port.outgoing;

import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import java.util.List;
import java.util.Optional;


public interface EmergencyRepositoryPort {

  Optional<Emergency> findById(Long id);

  List<Emergency> findByCenterIdOrderByCreatedAtDesc(Long centerId);

  List<Emergency> findByStatus(EmergencyStatus status);

  Emergency save(Emergency emergency);
}
