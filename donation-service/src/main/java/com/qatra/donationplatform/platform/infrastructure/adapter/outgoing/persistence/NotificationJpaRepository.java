package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
