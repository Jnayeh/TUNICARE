CREATE TABLE event_publication (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    completion_attempts INTEGER DEFAULT 0,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMPTZ NOT NULL,
    completion_date TIMESTAMPTZ,
    listener_id VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uq_event_publication ON event_publication (aggregate_type, aggregate_id, event_type, listener_id);
CREATE INDEX idx_event_publication_completion ON event_publication (completion_date);

