package com.qatra.donationplatform.profiles.core.domain.port.outgoing;

import com.qatra.donationplatform.profiles.core.domain.model.CenterAdminProfile;
import java.util.List;
import java.util.Optional;


public interface CenterAdminProfileRepositoryPort {

  Optional<CenterAdminProfile> findByUserId(Long userId);

  List<CenterAdminProfile> findByCenterId(Long centerId);

  CenterAdminProfile save(CenterAdminProfile profile);
}
