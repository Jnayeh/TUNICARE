package com.qatra.donationplatform.profiles.core.domain.model;

import java.time.Instant;


/** Class diagram: Session */
public class Session {

  private Long id;
  private Long userId;
  private String accessTokenHash;
  private String refreshTokenHash;
  private String ipAddress;
  private String userAgent;
  private Instant expiresAt;
  private Instant createdAt;

  public boolean validate() {
    return expiresAt != null && Instant.now().isBefore(expiresAt);
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

  public String getAccessTokenHash() {
    return accessTokenHash;
  }

  public void setAccessTokenHash(String accessTokenHash) {
    this.accessTokenHash = accessTokenHash;
  }

  public String getRefreshTokenHash() {
    return refreshTokenHash;
  }

  public void setRefreshTokenHash(String refreshTokenHash) {
    this.refreshTokenHash = refreshTokenHash;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
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
