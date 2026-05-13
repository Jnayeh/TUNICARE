package com.qatra.donationplatform.center.core.domain.port.outgoing;

import com.qatra.donationplatform.center.core.domain.model.BloodDonationCenter;
import java.util.List;
import java.util.Optional;


public interface BloodDonationCenterRepositoryPort {

  Optional<BloodDonationCenter> findById(Long id);

  List<BloodDonationCenter> findAllActive();

  BloodDonationCenter save(BloodDonationCenter center);
}
