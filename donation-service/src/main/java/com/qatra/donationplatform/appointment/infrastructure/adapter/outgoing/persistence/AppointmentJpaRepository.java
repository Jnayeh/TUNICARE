package com.qatra.donationplatform.appointment.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.AppointmentStatus;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {

  List<AppointmentEntity> findByDonorProfileIdOrderByScheduledTimeDesc(Long donorProfileId);

  List<AppointmentEntity> findByCenterIdAndScheduledTimeBetweenOrderByScheduledTime(
      Long centerId, Instant start, Instant end);

  List<AppointmentEntity> findByEmergencyId(Long emergencyId);

  List<AppointmentEntity> findByStatusAndConfirmedAtIsNullAndCreatedAtBefore(
      AppointmentStatus status, Instant cutoff);

  @Query(
      "SELECT a FROM AppointmentEntity a WHERE a.status IN ('SCHEDULED','CONFIRMED') "
          + "AND a.scheduledTime >= :from AND a.scheduledTime < :to")
  List<AppointmentEntity> findReminderWindow(@Param("from") Instant from, @Param("to") Instant to);
}
