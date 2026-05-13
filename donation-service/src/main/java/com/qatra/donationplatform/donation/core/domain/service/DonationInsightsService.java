package com.qatra.donationplatform.donation.core.domain.service;

import com.qatra.donationplatform.appointment.core.domain.model.Appointment;
import com.qatra.donationplatform.appointment.core.domain.port.outgoing.AppointmentRepositoryPort;
import com.qatra.donationplatform.donation.core.domain.port.incoming.DonationInsightsPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import com.qatra.donationplatform.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DonationInsightsService implements DonationInsightsPort {

  private final AppointmentRepositoryPort appointments;
  private final DonorProfileRepositoryPort donorProfiles;

  public DonationInsightsService(
      AppointmentRepositoryPort appointments, DonorProfileRepositoryPort donorProfiles) {
    this.appointments = appointments;
    this.donorProfiles = donorProfiles;
  }

  @Override
  public List<Appointment> completedDonationsForDonor(Long donorUserId) {
    var profile =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor"));
    return appointments.findByDonorProfileIdOrderByScheduledTimeDesc(profile.getId()).stream()
        .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
        .collect(Collectors.toList());
  }

  @Override
  public Map<String, Object> impactForDonor(Long donorUserId) {
    var d =
        donorProfiles
            .findByUserId(donorUserId)
            .orElseThrow(() -> new NotFoundException("Donor"));
    int lives = d.getTotalDonations() * 3;
    return Map.of("livesSavedEstimate", lives, "totalDonations", d.getTotalDonations());
  }

  @Override
  public Map<String, String> certificateLocation(Long donorUserId) {
    donorProfiles
        .findByUserId(donorUserId)
        .orElseThrow(() -> new NotFoundException("Donor"));
    return Map.of("downloadUrl", "/exports/donation-certificate-" + donorUserId + ".pdf");
  }
}
