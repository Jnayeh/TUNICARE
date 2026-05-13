package com.qatra.notification.infrastructure.persistence;

import jakarta.inject.Singleton;
import java.util.UUID;
import org.jooq.DSLContext;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/** Outbound adapter: records deliveries for SYS17 tracking (status transitions can extend here). */
@Singleton
public class JooqNotificationDeliveryRepository {

  private final DSLContext dsl;

  public JooqNotificationDeliveryRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  public void insertDelivery(
      UUID id, UUID userId, String channel, String status, String title, String body, String rawJson) {
    dsl.insertInto(table("notification_deliveries"))
        .columns(
            field("id"),
            field("user_id"),
            field("channel"),
            field("status"),
            field("title"),
            field("body"),
            field("raw_payload"))
        .values(id, userId, channel, status, title, body, rawJson)
        .execute();
  }
}
