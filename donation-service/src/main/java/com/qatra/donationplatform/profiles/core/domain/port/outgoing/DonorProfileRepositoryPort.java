package com.qatra.donationplatform.profiles.core.domain.port.outgoing;

import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface DonorProfileRepositoryPort {

  Optional<DonorProfile> findById(Long id);

  Optional<DonorProfile> findByUserId(Long userId);

  List<DonorProfile> findByAvailability(AvailabilityStatus availability);

  List<DonorProfile> findByEligibleFromDate(LocalDate date);

  DonorProfile save(DonorProfile profile);
}
