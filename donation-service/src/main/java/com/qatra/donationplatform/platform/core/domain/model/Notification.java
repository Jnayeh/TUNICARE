package com.qatra.donationplatform.platform.core.domain.model;

import com.qatra.donationplatform.shared.domain.enums.NotificationChannel;
import com.qatra.donationplatform.shared.domain.enums.NotificationStatus;
import com.qatra.donationplatform.shared.domain.enums.NotificationType;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/** Class diagram: Notification */
public class Notification {

  private Long id;
  private Long userId;
  private NotificationType type;
  private String title;
  private String body;
  private Map<String, Object> data = new HashMap<>();
  private NotificationChannel channel;
  private NotificationStatus status;
  private Instant createdAt;
  private Instant sentAt;
  private Instant readAt;

  public void send() {
    this.status = NotificationStatus.SENT;
    this.sentAt = Instant.now();
  }

  public void markAsRead() {
    this.status = NotificationStatus.READ;
    this.readAt = Instant.now();
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

  public NotificationType getType() {
    return type;
  }

  public void setType(NotificationType type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data != null ? new HashMap<>(data) : new HashMap<>();
  }

  public NotificationChannel getChannel() {
    return channel;
  }

  public void setChannel(NotificationChannel channel) {
    this.channel = channel;
  }

  public NotificationStatus getStatus() {
    return status;
  }

  public void setStatus(NotificationStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getSentAt() {
    return sentAt;
  }

  public void setSentAt(Instant sentAt) {
    this.sentAt = sentAt;
  }

  public Instant getReadAt() {
    return readAt;
  }

  public void setReadAt(Instant readAt) {
    this.readAt = readAt;
  }
}
