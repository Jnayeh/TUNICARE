package com.qatra.donationplatform.platform.core.domain.port.incoming;

import com.qatra.donationplatform.platform.core.domain.model.Notification;
import java.util.List;


public interface UserNotificationsPort {

  List<Notification> listRecentForUser(Long userId);
}
