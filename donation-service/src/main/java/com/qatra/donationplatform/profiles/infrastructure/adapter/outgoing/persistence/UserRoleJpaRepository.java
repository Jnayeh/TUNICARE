package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {
  List<UserRoleEntity> findByUserId(Long userId);
}
