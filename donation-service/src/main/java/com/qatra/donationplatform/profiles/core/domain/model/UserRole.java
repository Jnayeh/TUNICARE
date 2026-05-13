package com.qatra.donationplatform.profiles.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.Role;
import java.time.Instant;


/** Class diagram: UserRole */
public class UserRole {

  private Long id;
  private Long userId;
  private Role role;
  private Long contextId;
  private String contextType;
  private Instant assignedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Long getContextId() {
    return contextId;
  }

  public void setContextId(Long contextId) {
    this.contextId = contextId;
  }

  public String getContextType() {
    return contextType;
  }

  public void setContextType(String contextType) {
    this.contextType = contextType;
  }

  public Instant getAssignedAt() {
    return assignedAt;
  }

  public void setAssignedAt(Instant assignedAt) {
    this.assignedAt = assignedAt;
  }
}
