package com.qatra.donationplatform.profiles.core.domain.port.outgoing;

import com.qatra.donationplatform.profiles.core.domain.model.UserRole;
import java.util.List;


public interface UserRoleRepositoryPort {

  UserRole save(UserRole role);

  void deleteById(Long id);

  List<UserRole> findByUserId(Long userId);
}
