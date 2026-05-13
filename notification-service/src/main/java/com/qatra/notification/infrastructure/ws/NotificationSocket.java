package com.qatra.notification.infrastructure.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;

@ServerWebSocket("/ws/notifications/{userId}")
public class NotificationSocket {

  private final NotificationHub hub;

  public NotificationSocket(NotificationHub hub) {
    this.hub = hub;
  }

  @OnOpen
  public void onOpen(String userId, WebSocketSession session, HttpRequest request) {
    hub.register(userId, session);
  }

  @OnMessage
  public void onMessage(String userId, String message) {
    // Clients may send ping / ack; out-of-scope for core use cases.
  }

  @OnClose
  public void onClose(String userId) {
    hub.remove(userId);
  }
}
