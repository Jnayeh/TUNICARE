package com.qatra.donationplatform.emergency.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.emergency.core.domain.model.Emergency;
import org.springframework.stereotype.Component;

@Component
public class EmergencyPersistenceMapper {

  public Emergency toDomain(EmergencyEntity e) {
    Emergency x = new Emergency();
    x.setId(e.getId());
    x.setCenterId(e.getCenterId());
    x.setCreatedByStaffProfileId(e.getCreatedByStaffProfileId());
    x.setBloodTypeNeeded(e.getBloodTypeNeeded());
    x.setUnitsNeeded(e.getUnitsNeeded());
    x.setUnitsCollected(e.getUnitsCollected());
    x.setUrgency(e.getUrgency());
    x.setContactPerson(e.getContactPerson());
    x.setContactPhone(e.getContactPhone());
    x.setPatientInfo(e.getPatientInfo());
    x.setStatus(e.getStatus());
    x.setMatchRadius(e.getMatchRadius());
    x.setNeededBy(e.getNeededBy());
    x.setCreatedAt(e.getCreatedAt());
    x.setResolvedAt(e.getResolvedAt());
    x.setResolvedByStaffProfileId(e.getResolvedByStaffProfileId());
    return x;
  }

  public EmergencyEntity toEntity(Emergency x) {
    EmergencyEntity e = new EmergencyEntity();
    e.setId(x.getId());
    e.setCenterId(x.getCenterId());
    e.setCreatedByStaffProfileId(x.getCreatedByStaffProfileId());
    e.setBloodTypeNeeded(x.getBloodTypeNeeded());
    e.setUnitsNeeded(x.getUnitsNeeded());
    e.setUnitsCollected(x.getUnitsCollected());
    e.setUrgency(x.getUrgency());
    e.setContactPerson(x.getContactPerson());
    e.setContactPhone(x.getContactPhone());
    e.setPatientInfo(x.getPatientInfo());
    e.setStatus(x.getStatus());
    e.setMatchRadius(x.getMatchRadius());
    e.setNeededBy(x.getNeededBy());
    e.setCreatedAt(x.getCreatedAt());
    e.setResolvedAt(x.getResolvedAt());
    e.setResolvedByStaffProfileId(x.getResolvedByStaffProfileId());
    return e;
  }
}
