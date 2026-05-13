package com.qatra.donationplatform.donation.core.domain.port.incoming;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import java.util.List;
import java.util.Map;


/** Read-side use cases around completed donations (history, impact), without a separate diagram entity. */
public interface DonationInsightsPort {

  List<Appointment> completedDonationsForDonor(Long donorUserId);

  Map<String, Object> impactForDonor(Long donorUserId);

  Map<String, String> certificateLocation(Long donorUserId);
}
