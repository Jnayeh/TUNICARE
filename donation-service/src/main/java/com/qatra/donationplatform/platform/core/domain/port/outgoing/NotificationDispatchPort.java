package com.qatra.donationplatform.platform.core.domain.port.outgoing;

import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import java.util.Map;


public interface NotificationDispatchPort {

  void dispatch(
      Long userId,
      NotificationType type,
      NotificationChannel channel,
      String title,
      String body,
      Map<String, Object> data);
}
