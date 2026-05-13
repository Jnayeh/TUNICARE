package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.UserRole;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRoleRepositoryPort;
import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserRoleRepositoryAdapter implements UserRoleRepositoryPort {

  private final UserRoleJpaRepository jpa;
  private final ProfilesPersistenceMapper mapper;

  public UserRoleRepositoryAdapter(UserRoleJpaRepository jpa, ProfilesPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public UserRole save(UserRole role) {
    return mapper.toDomain(jpa.save(mapper.toEntity(role)));
  }

  @Override
  public void deleteById(Long id) {
    jpa.deleteById(id);
  }

  @Override
  public List<UserRole> findByUserId(Long userId) {
    return jpa.findByUserId(userId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }
}
