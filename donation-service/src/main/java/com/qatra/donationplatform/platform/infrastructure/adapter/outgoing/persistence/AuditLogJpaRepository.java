package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {
  List<AuditLogEntity> findTop200ByOrderByTimestampDesc();
}
