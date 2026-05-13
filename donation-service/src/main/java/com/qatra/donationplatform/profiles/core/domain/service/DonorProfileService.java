package com.qatra.donationplatform.profiles.core.domain.service;

import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.profiles.core.domain.port.incoming.DonorProfilePort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import com.qatra.donationplatform.shared.exception.NotFoundException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonorProfileService implements DonorProfilePort {

  private final DonorProfileRepositoryPort donorProfiles;

  public DonorProfileService(DonorProfileRepositoryPort donorProfiles) {
    this.donorProfiles = donorProfiles;
  }

  @Override
  public DonorProfile requireForUser(Long userId) {
    return donorProfiles
        .findByUserId(userId)
        .orElseThrow(() -> new NotFoundException("Donor profile not found"));
  }

  @Override
  @Transactional
  public DonorProfile updateHealthQuestionnaire(Long userId, Map<String, Object> questionnaire) {
    DonorProfile d = requireForUser(userId);
    d.setHealthQuestionnaire(questionnaire);
    Object perm = questionnaire != null ? questionnaire.get("permanentRestriction") : null;
    if (perm instanceof Boolean b && b) {
      d.setPermanentRestriction(true);
      d.setAvailability(AvailabilityStatus.PERMANENTLY_RESTRICTED);
    }
    d.setProfileComplete(questionnaire != null && !questionnaire.isEmpty());
    return donorProfiles.save(d);
  }

  @Override
  @Transactional
  public DonorProfile updateLocation(
      Long userId, Double lat, Double lon, String address, String city, String country) {
    DonorProfile d = requireForUser(userId);
    d.updateLocation(lat, lon, address, city, country);
    return donorProfiles.save(d);
  }

  @Override
  @Transactional
  public DonorProfile updateNotificationPreferences(
      Long userId, Map<String, Object> prefs, NotificationFrequency frequency) {
    DonorProfile d = requireForUser(userId);
    d.updateNotificationPreferences(prefs, frequency);
    return donorProfiles.save(d);
  }

  @Override
  @Transactional
  public DonorProfile updateAvailability(Long userId, AvailabilityStatus status) {
    DonorProfile d = requireForUser(userId);
    d.setAvailability(status);
    return donorProfiles.save(d);
  }

  @Override
  @Transactional
  public DonorProfile setBloodType(Long userId, BloodType bloodType) {
    DonorProfile d = requireForUser(userId);
    d.setBloodType(bloodType);
    return donorProfiles.save(d);
  }

  @Override
  @Transactional
  public void applyReliabilityDelta(Long donorProfileId, double delta) {
    DonorProfile d =
        donorProfiles
            .findById(donorProfileId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    d.calculateReliability(delta);
    donorProfiles.save(d);
  }

  @Override
  @Transactional
  public void applyDonationRecorded(Long donorProfileId, int mlCollected, int cooldownDays) {
    DonorProfile d =
        donorProfiles
            .findById(donorProfileId)
            .orElseThrow(() -> new NotFoundException("Donor profile not found"));
    LocalDate day = LocalDate.now(ZoneOffset.UTC);
    d.recordDonation(mlCollected, day, cooldownDays);
    d.calculateReliability(6d);
    donorProfiles.save(d);
  }
}
