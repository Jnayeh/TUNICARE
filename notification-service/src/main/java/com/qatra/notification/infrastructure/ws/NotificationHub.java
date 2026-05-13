package com.qatra.notification.infrastructure.ws;

import io.micronaut.websocket.CloseReason;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class NotificationHub {

  private static final Logger LOG = LoggerFactory.getLogger(NotificationHub.class);

  private final Map<String, WebSocketSession> sessionsByUser = new ConcurrentHashMap<>();

  public void register(String userId, WebSocketSession session) {
    sessionsByUser.put(userId, session);
    LOG.debug("WS connected user={}", userId);
  }

  public void remove(String userId) {
    sessionsByUser.remove(userId);
  }

  public void broadcast(String userId, String jsonPayload) {
    WebSocketSession s = sessionsByUser.get(userId);
    if (s != null && s.isOpen()) {
      s.sendSync(jsonPayload);
    }
  }

  public void closeQuietly(String userId) {
    WebSocketSession s = sessionsByUser.get(userId);
    if (s != null) {
      s.close(CloseReason.NORMAL);
    }
  }
}
