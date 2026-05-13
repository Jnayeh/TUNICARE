package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.platform.core.domain.port.outgoing.SystemConfigurationPort;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SystemConfigurationAdapter implements SystemConfigurationPort {

  private final SystemConfigJpaRepository repo;

  public SystemConfigurationAdapter(SystemConfigJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public int getCooldownDays() {
    return getInt("cooldown.days", 56);
  }

  @Override
  public int getDefaultMatchRadiusKm() {
    return getInt("default.match.radius.km", 25);
  }

  @Override
  public int getAppointmentConfirmTimeoutHours() {
    return getInt("appointment.confirm.timeout.hours", 24);
  }

  private int getInt(String key, int defaultValue) {
    Optional<SystemConfigEntity> row = repo.findByConfigKeyAndActiveTrue(key);
    return row
        .map(SystemConfigEntity::getConfigValue)
        .map(m -> m.get("value"))
        .map(
            v -> {
              if (v instanceof Number n) {
                return n.intValue();
              }
              if (v instanceof String s) {
                return Integer.parseInt(s);
              }
              throw new IllegalStateException("Invalid config value for " + key);
            })
        .orElse(defaultValue);
  }
}
