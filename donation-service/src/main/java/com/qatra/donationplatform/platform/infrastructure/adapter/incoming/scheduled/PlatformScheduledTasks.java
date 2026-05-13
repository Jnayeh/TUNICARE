package com.qatra.donationplatform.platform.infrastructure.adapter.incoming.scheduled;

import com.qatra.donationplatform.appointment.core.domain.port.incoming.AppointmentSchedulingPort;
import com.qatra.donationplatform.platform.core.domain.port.outgoing.NotificationDispatchPort;
import com.qatra.donationplatform.profiles.core.domain.port.outgoing.DonorProfileRepositoryPort;
import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlatformScheduledTasks {

  private final DonorProfileRepositoryPort donorProfiles;
  private final NotificationDispatchPort notifications;
  private final AppointmentSchedulingPort appointments;

  @Scheduled(cron = "0 0 7 * * *")
  public void eligibilityReminders() {
    LocalDate today = LocalDate.now();
    donorProfiles.findByEligibleFromDate(today).stream()
        .forEach(
            d ->
                notifications.dispatch(
                    d.getUserId(),
                    NotificationType.ELIGIBILITY_REMINDER,
                    NotificationChannel.IN_APP,
                    "You can donate again",
                    "Your donation cooldown has ended.",
                    new HashMap<>()));
  }

  @Scheduled(fixedRate = 3_600_000)
  public void appointmentReminders24h() {
    appointments.sendUpcomingReminders(Instant.now());
  }

  @Scheduled(fixedRate = 1_800_000)
  public void releaseStaleAppointments() {
    appointments.releaseUnconfirmedSlots(Instant.now());
  }
}
