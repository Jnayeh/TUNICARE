package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.profiles.core.domain.model.User;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.UserRepositoryPort;
import java.util.Collection;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository jpa;
  private final ProfilesPersistenceMapper mapper;

  public UserRepositoryAdapter(UserJpaRepository jpa, ProfilesPersistenceMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<User> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpa.findByEmailIgnoreCase(email).map(mapper::toDomain);
  }

  @Override
  public User save(User user) {
    return mapper.toDomain(jpa.save(mapper.toEntity(user)));
  }

  @Override
  public Collection<User> findAll() {
    return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
  }
}
