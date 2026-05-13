package com.qatra.donationplatform.profiles.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.UserStatus;
import java.time.Instant;


/** Class diagram: User */
public class User {

  private Long id;
  private String email;
  private String phone;
  private String hashedPassword;
  private String displayName;
  private UserStatus status;
  private boolean emailVerified;
  private Instant createdAt;
  private Instant lastActiveAt;

  public void verifyEmail() {
    this.emailVerified = true;
    if (this.status == UserStatus.PENDING_VERIFICATION) {
      this.status = UserStatus.ACTIVE;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getLastActiveAt() {
    return lastActiveAt;
  }

  public void setLastActiveAt(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }
}
