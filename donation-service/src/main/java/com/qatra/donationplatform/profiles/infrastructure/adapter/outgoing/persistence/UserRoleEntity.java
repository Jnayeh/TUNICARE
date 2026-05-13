package com.qatra.donationplatform.profiles.infrastructure.adapter.outgoing.persistence;

import com.qatra.donationplatform.shared.domain.enums.Role;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
 private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "context_id")
  private Long contextId;

  @Column(name = "context_type")
  private String contextType;

  @Column(name = "assigned_at", nullable = false)
  private Instant assignedAt;

  @PrePersist
  void prePersist() {

    if (assignedAt == null) {
      assignedAt = Instant.now();
    }
  }
}
