CREATE TABLE notification_deliveries (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    channel VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    title TEXT,
    body TEXT,
    raw_payload TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_notification_deliveries_user ON notification_deliveries (user_id, created_at DESC);
