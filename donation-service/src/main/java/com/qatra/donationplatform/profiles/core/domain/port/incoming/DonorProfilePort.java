package com.qatra.donationplatform.profiles.core.domain.port.incoming;

import com.qatra.donationplatform.profiles.core.domain.model.DonorProfile;
import com.qatra.donationplatform.shared.domain.enums.AvailabilityStatus;
import com.qatra.donationplatform.shared.domain.enums.BloodType;
import com.qatra.donationplatform.shared.domain.enums.NotificationFrequency;
import java.util.Map;


public interface DonorProfilePort {

  DonorProfile requireForUser(Long userId);

  DonorProfile updateHealthQuestionnaire(Long userId, Map<String, Object> questionnaire);

  DonorProfile updateLocation(
      Long userId, Double lat, Double lon, String address, String city, String country);

  DonorProfile updateNotificationPreferences(
      Long userId, Map<String, Object> prefs, NotificationFrequency frequency);

  DonorProfile updateAvailability(Long userId, AvailabilityStatus status);

  DonorProfile setBloodType(Long userId, BloodType bloodType);

  void applyReliabilityDelta(Long donorProfileId, double delta);

  void applyDonationRecorded(Long donorProfileId, int mlCollected, int cooldownDays);
}
