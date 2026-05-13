package com.qatra.donationplatform.profiles.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.VerificationTokenType;
import java.time.Instant;


/** Class diagram: VerificationToken */
public class VerificationToken {

  private Long id;
  private Long userId;
  private String tokenHash;
  private VerificationTokenType type;
  private Instant expiresAt;
  private Instant createdAt;

  public boolean validate() {
    return expiresAt != null && Instant.now().isBefore(expiresAt);
  }

  public void consume() {
    expiresAt = Instant.now();
  }

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

  public String getTokenHash() {
    return tokenHash;
  }

  public void setTokenHash(String tokenHash) {
    this.tokenHash = tokenHash;
  }

  public VerificationTokenType getType() {
    return type;
  }

  public void setType(VerificationTokenType type) {
    this.type = type;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
