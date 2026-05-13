package com.qatra.donationplatform.center.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import com.qatra.donationplatform.center.core.domain.port.outgoing.BloodDonationCenterRepositoryPort;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BloodDonationCenterRepositoryAdapter implements BloodDonationCenterRepositoryPort {

  private final BloodDonationCenterJpaRepository jpa;
  private final CenterPersistenceMapper mapper;

  public BloodDonationCenterRepositoryAdapter(
      BloodDonationCenterJpaRepository jpa, CenterPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<BloodDonationCenter> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<BloodDonationCenter> findAllActive() {
    return jpa.findByActiveTrue().stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public BloodDonationCenter save(BloodDonationCenter center) {
    return mapper.toDomain(jpa.save(mapper.toEntity(center)));
  }
}
