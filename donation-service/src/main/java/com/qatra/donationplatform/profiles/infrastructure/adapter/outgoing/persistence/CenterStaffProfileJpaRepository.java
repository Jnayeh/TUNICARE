package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterStaffProfileJpaRepository extends JpaRepository<CenterStaffProfileEntity, Long> {
  Optional<CenterStaffProfileEntity> findByUserId(Long userId);

  List<CenterStaffProfileEntity> findByCenterId(Long centerId);
}
