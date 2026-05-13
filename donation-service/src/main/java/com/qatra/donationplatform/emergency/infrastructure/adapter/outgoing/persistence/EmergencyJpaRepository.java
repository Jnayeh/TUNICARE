package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyJpaRepository extends JpaRepository<EmergencyEntity, Long> {
  List<EmergencyEntity> findByCenterIdOrderByCreatedAtDesc(Long centerId);

  List<EmergencyEntity> findByStatus(EmergencyStatus status);
}
