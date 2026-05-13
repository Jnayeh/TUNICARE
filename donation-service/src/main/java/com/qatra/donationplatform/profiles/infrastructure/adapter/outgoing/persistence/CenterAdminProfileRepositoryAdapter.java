package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterAdminProfileRepositoryPort;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CenterAdminProfileRepositoryAdapter implements CenterAdminProfileRepositoryPort {

  private final CenterAdminProfileJpaRepository jpa;
  private final ProfilesPersistenceMapper mapper;

  public CenterAdminProfileRepositoryAdapter(
      CenterAdminProfileJpaRepository jpa, ProfilesPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<CenterAdminProfile> findByUserId(Long userId) {
    return jpa.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public List<CenterAdminProfile> findByCenterId(Long centerId) {
    return jpa.findByCenterId(centerId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public CenterAdminProfile save(CenterAdminProfile profile) {
    return mapper.toDomain(jpa.save(mapper.toEntity(profile)));
  }
}
