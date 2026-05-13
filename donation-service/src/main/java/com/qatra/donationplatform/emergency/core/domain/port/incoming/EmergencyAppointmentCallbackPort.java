package com.qatra.donationplatform.emergency.core.domain.port.incoming;



/** Called by appointment module when a donor books an appointment tied to an emergency. */
public interface EmergencyAppointmentCallbackPort {

  void onEmergencyAppointment(Long emergencyId, Long donorProfileId);
}
