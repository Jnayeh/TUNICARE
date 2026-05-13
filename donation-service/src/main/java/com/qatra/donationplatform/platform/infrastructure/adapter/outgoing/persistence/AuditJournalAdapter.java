package com.qatra.donationplatform.platform.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.platform.core.domain.port.outgoing.AuditJournalPort;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditJournalAdapter implements AuditJournalPort {

  private final AuditLogJpaRepository repo;

  public AuditJournalAdapter(AuditLogJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  @Transactional
  public void record(
      Long actorUserId,
      String action,
      String entityType,
      Long entityId,
      Map<String, Object> oldValue,
      Map<String, Object> newValue,
      String ipAddress,
      String userAgent) {
    AuditLogEntity log = new AuditLogEntity();
    log.setUserId(actorUserId);
    log.setAction(action);
    log.setEntityType(entityType);
    log.setEntityId(entityId);
    log.setOldValue(oldValue);
    log.setNewValue(newValue);
    log.setIpAddress(ipAddress);
    log.setUserAgent(userAgent);
    repo.save(log);
  }
}
