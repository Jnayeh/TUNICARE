package com.qatra.donationplatform;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.qatra.donationplatform")
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "Qatra Donation Platform API",
        version = "1.0",
        description = "Blood donation platform API for managing donors, centers, appointments, and emergencies"
    )
)
public class DonationPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(DonationPlatformApplication.class, args);
  }
}
