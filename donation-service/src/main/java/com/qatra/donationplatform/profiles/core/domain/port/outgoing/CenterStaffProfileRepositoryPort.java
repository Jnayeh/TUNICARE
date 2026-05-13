package com.qatra.donationplatform.profiles.core.domain.port.outgoing;

import com.qatra.donationplatform.profiles.core.domain.model.CenterStaffProfile;
import java.util.List;
import java.util.Optional;


public interface CenterStaffProfileRepositoryPort {

  Optional<CenterStaffProfile> findById(Long id);

  Optional<CenterStaffProfile> findByUserId(Long userId);

  List<CenterStaffProfile> findByCenterId(Long centerId);

  CenterStaffProfile save(CenterStaffProfile profile);

  void delete(CenterStaffProfile profile);
}
