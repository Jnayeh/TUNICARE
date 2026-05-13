package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import com.qatra.donationplatform.emergency.core.domain.port.outgoing.EmergencyRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.EmergencyStatus;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmergencyRepositoryAdapter implements EmergencyRepositoryPort {

  private final EmergencyJpaRepository jpa;
  private final EmergencyPersistenceMapper mapper;

  public EmergencyRepositoryAdapter(EmergencyJpaRepository jpa, EmergencyPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<Emergency> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Emergency> findByCenterIdOrderByCreatedAtDesc(Long centerId) {
    return jpa.findByCenterIdOrderByCreatedAtDesc(centerId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Emergency> findByStatus(EmergencyStatus status) {
    return jpa.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public Emergency save(Emergency emergency) {
    return mapper.toDomain(jpa.save(mapper.toEntity(emergency)));
  }
}
