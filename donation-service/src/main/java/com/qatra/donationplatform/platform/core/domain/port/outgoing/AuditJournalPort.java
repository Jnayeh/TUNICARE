package com.qatra.donationplatform.platform.core.domain.port.outgoing;

import java.util.Map;


public interface AuditJournalPort {

  void record(
      Long actorUserId,
      String action,
      String entityType,
      Long entityId,
      Map<String, Object> oldValue,
      Map<String, Object> newValue,
      String ipAddress,
      String userAgent);
}
