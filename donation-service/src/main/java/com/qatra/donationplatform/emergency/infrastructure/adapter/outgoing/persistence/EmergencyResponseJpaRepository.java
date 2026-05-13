package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyResponseJpaRepository extends JpaRepository<EmergencyResponseEntity, Long> {
  Optional<EmergencyResponseEntity> findByEmergencyIdAndDonorProfileId(
      Long emergencyId, Long donorProfileId);

  List<EmergencyResponseEntity> findByEmergencyId(Long emergencyId);

  List<EmergencyResponseEntity> findByDonorProfileId(Long donorProfileId);

  long countByEmergencyId(Long emergencyId);
}
