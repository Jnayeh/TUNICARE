package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.CenterStaffProfileRepositoryPort;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CenterStaffProfileRepositoryAdapter implements CenterStaffProfileRepositoryPort {

  private final CenterStaffProfileJpaRepository jpa;
  private final ProfilesPersistenceMapper mapper;

  public CenterStaffProfileRepositoryAdapter(
      CenterStaffProfileJpaRepository jpa, ProfilesPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<CenterStaffProfile> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<CenterStaffProfile> findByUserId(Long userId) {
    return jpa.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public List<CenterStaffProfile> findByCenterId(Long centerId) {
    return jpa.findByCenterId(centerId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public CenterStaffProfile save(CenterStaffProfile profile) {
    return mapper.toDomain(jpa.save(mapper.toEntity(profile)));
  }

  @Override
  public void delete(CenterStaffProfile profile) {
    jpa.delete(mapper.toEntity(profile));
  }
}
