package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.UserStatus;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column private String email;
  @Column private String phone;

  @Column(name = "hashed_password")
  private String hashedPassword;

  @Column(name = "display_name")
  private String displayName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status;

  @Column(name = "email_verified", nullable = false)
  private boolean emailVerified;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "last_active_at")
  private Instant lastActiveAt;

  @PrePersist
  void prePersist() {

    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
