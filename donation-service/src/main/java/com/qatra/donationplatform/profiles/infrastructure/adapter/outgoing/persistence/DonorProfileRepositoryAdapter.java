package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DonorProfileRepositoryAdapter implements DonorProfileRepositoryPort {

  private final DonorProfileJpaRepository jpa;
  private final ProfilesPersistenceMapper mapper;

  public DonorProfileRepositoryAdapter(DonorProfileJpaRepository jpa, ProfilesPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<DonorProfile> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<DonorProfile> findByUserId(Long userId) {
    return jpa.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public List<DonorProfile> findByAvailability(AvailabilityStatus availability) {
    return jpa.findByAvailability(availability).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public List<DonorProfile> findByEligibleFromDate(LocalDate date) {
    return jpa.findByEligibleFromDate(date).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public DonorProfile save(DonorProfile profile) {
    return mapper.toDomain(jpa.save(mapper.toEntity(profile)));
  }
}
