package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigJpaRepository extends JpaRepository<SystemConfigEntity, Long> {
  Optional<SystemConfigEntity> findByConfigKeyAndActiveTrue(String configKey);
}
