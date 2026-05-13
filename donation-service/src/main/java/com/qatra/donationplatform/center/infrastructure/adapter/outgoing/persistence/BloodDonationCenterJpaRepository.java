package com.qatra.donationplatform.center.infrastructure.adapter.outgoing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodDonationCenterJpaRepository extends JpaRepository<BloodDonationCenterEntity, Long> {
  List<BloodDonationCenterEntity> findByActiveTrue();
}
