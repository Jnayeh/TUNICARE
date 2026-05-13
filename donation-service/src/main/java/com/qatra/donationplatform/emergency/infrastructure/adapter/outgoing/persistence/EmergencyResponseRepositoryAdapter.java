package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.emergency.core.domain.model.EmergencyResponse;
import com.qatra.donationplatform.emergency.core.domain.port.outgoing.EmergencyResponseRepositoryPort;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmergencyResponseRepositoryAdapter implements EmergencyResponseRepositoryPort {

  private final EmergencyResponseJpaRepository jpa;
  private final EmergencyResponsePersistenceMapper mapper;

  public EmergencyResponseRepositoryAdapter(
      EmergencyResponseJpaRepository jpa, EmergencyResponsePersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<EmergencyResponse> findByEmergencyIdAndDonorProfileId(
      Long emergencyId, Long donorProfileId) {
    return jpa.findByEmergencyIdAndDonorProfileId(emergencyId, donorProfileId).map(mapper::toDomain);
  }

  @Override
  public List<EmergencyResponse> findByEmergencyId(Long emergencyId) {
    return jpa.findByEmergencyId(emergencyId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public List<EmergencyResponse> findByDonorProfileId(Long donorProfileId) {
    return jpa.findByDonorProfileId(donorProfileId).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public EmergencyResponse save(EmergencyResponse response) {
    return mapper.toDomain(jpa.save(mapper.toEntity(response)));
  }

  @Override
  public long countByEmergencyId(Long emergencyId) {
    return jpa.countByEmergencyId(emergencyId);
  }
}
