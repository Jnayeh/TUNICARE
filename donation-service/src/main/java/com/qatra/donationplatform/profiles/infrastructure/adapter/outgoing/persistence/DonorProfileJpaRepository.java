package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorProfileJpaRepository extends JpaRepository<DonorProfileEntity, Long> {
  Optional<DonorProfileEntity> findByUserId(Long userId);

  List<DonorProfileEntity> findByAvailability(AvailabilityStatus availability);

  List<DonorProfileEntity> findByEligibleFromDate(LocalDate eligibleFromDate);
}
