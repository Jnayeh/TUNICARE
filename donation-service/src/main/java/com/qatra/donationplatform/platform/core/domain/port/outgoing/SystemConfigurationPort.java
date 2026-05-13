package com.qatra.donationplatform.platform.core.domain.port.outgoing;

public interface SystemConfigurationPort {

  int getCooldownDays();

  int getDefaultMatchRadiusKm();

  int getAppointmentConfirmTimeoutHours();
}
