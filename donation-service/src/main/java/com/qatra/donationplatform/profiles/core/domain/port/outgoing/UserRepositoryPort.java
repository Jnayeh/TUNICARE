package com.qatra.donationplatform.profiles.core.domain.port.outgoing;

import com.qatra.donationplatform.profiles.core.domain.model.User;
import java.util.Collection;
import java.util.Optional;


public interface UserRepositoryPort {

  Optional<User> findById(Long id);

  Optional<User> findByEmail(String email);

  User save(User user);

  Collection<User> findAll();
}
