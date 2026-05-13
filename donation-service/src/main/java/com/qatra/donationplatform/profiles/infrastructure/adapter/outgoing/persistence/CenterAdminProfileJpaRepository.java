package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterAdminProfileJpaRepository extends JpaRepository<CenterAdminProfileEntity, Long> {
  Optional<CenterAdminProfileEntity> findByUserId(Long userId);

  List<CenterAdminProfileEntity> findByCenterId(Long centerId);
}
