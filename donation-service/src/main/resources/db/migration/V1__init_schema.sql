
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(320),
    phone VARCHAR(64),
    hashed_password VARCHAR(255),
    display_name VARCHAR(255),
    status VARCHAR(32) NOT NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    last_active_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX uq_users_email ON users (email) WHERE email IS NOT NULL;
CREATE UNIQUE INDEX uq_users_phone ON users (phone) WHERE phone IS NOT NULL;

CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role VARCHAR(32) NOT NULL,
    context_id BIGINT,
    context_type VARCHAR(64),
    assigned_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_user_roles_user ON user_roles (user_id);

CREATE TABLE sessions (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    access_token_hash VARCHAR(255) NOT NULL,
    refresh_token_hash VARCHAR(255),
    ip_address VARCHAR(64),
    user_agent VARCHAR(512),
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_sessions_user ON sessions (user_id);

CREATE TABLE verification_tokens (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    type VARCHAR(32) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_verification_tokens_user ON verification_tokens (user_id);

CREATE TABLE blood_donation_centers (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    address VARCHAR(512),
    city VARCHAR(128),
    country VARCHAR(128),
    postal_code VARCHAR(32),
    phone VARCHAR(64),
    email VARCHAR(320),
    operating_hours JSONB,
    daily_capacity INTEGER NOT NULL DEFAULT 0,
    facility_type VARCHAR(32) NOT NULL,
    amenities JSONB,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    created_by_user_id BIGINT REFERENCES users (id)
);

CREATE TABLE donor_profiles (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    blood_type VARCHAR(32),
    availability VARCHAR(32) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    address VARCHAR(512),
    city VARCHAR(128),
    country VARCHAR(128),
    notification_preferences JSONB,
    max_notification_distance INTEGER,
    notification_frequency VARCHAR(32) NOT NULL,
    allow_emergency_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    permanent_restriction BOOLEAN NOT NULL DEFAULT FALSE,
    last_donation_date DATE,
    eligible_from_date DATE,
    total_donations INTEGER NOT NULL DEFAULT 0,
    total_ml_donated INTEGER NOT NULL DEFAULT 0,
    reliability_score DOUBLE PRECISION NOT NULL DEFAULT 100.0,
    health_questionnaire JSONB,
    profile_complete BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_donor_profiles_location ON donor_profiles (latitude, longitude);
CREATE INDEX idx_donor_profiles_eligible ON donor_profiles (eligible_from_date);

CREATE TABLE center_staff_profiles (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    center_id BIGINT NOT NULL REFERENCES blood_donation_centers (id) ON DELETE CASCADE,
    department VARCHAR(128),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE center_admin_profiles (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    center_id BIGINT NOT NULL REFERENCES blood_donation_centers (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE appointments (
    id BIGINT PRIMARY KEY,
    donor_profile_id BIGINT NOT NULL REFERENCES donor_profiles (id) ON DELETE CASCADE,
    center_id BIGINT NOT NULL REFERENCES blood_donation_centers (id),
    emergency_id BIGINT,
    scheduled_time TIMESTAMPTZ NOT NULL,
    estimated_duration INTEGER NOT NULL DEFAULT 60,
    status VARCHAR(32) NOT NULL,
    appointment_type VARCHAR(32) NOT NULL,
    ml_collected INTEGER,
    notes TEXT,
    cancellation_reason TEXT,
    completed_by_staff_profile_id BIGINT REFERENCES center_staff_profiles (id),
    created_at TIMESTAMPTZ NOT NULL,
    confirmed_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    cancelled_at TIMESTAMPTZ
);

CREATE INDEX idx_appointments_center_time ON appointments (center_id, scheduled_time);
CREATE INDEX idx_appointments_donor ON appointments (donor_profile_id);

CREATE TABLE emergencies (
    id BIGINT PRIMARY KEY,
    center_id BIGINT NOT NULL REFERENCES blood_donation_centers (id),
    created_by_staff_profile_id BIGINT NOT NULL REFERENCES center_staff_profiles (id),
    blood_type_needed VARCHAR(32) NOT NULL,
    units_needed INTEGER NOT NULL,
    units_collected INTEGER NOT NULL DEFAULT 0,
    urgency VARCHAR(32) NOT NULL,
    contact_person VARCHAR(255),
    contact_phone VARCHAR(64),
    patient_info TEXT,
    status VARCHAR(32) NOT NULL,
    match_radius INTEGER,
    needed_by TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    resolved_at TIMESTAMPTZ,
    resolved_by_staff_profile_id BIGINT REFERENCES center_staff_profiles (id)
);

CREATE INDEX idx_emergencies_center_status ON emergencies (center_id, status);

CREATE TABLE emergency_responses (
    id BIGINT PRIMARY KEY,
    emergency_id BIGINT NOT NULL REFERENCES emergencies (id) ON DELETE CASCADE,
    donor_profile_id BIGINT NOT NULL REFERENCES donor_profiles (id) ON DELETE CASCADE,
    response_type VARCHAR(32) NOT NULL,
    message TEXT,
    responded_at TIMESTAMPTZ,
    notified_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX uq_emergency_response ON emergency_responses (emergency_id, donor_profile_id);

CREATE TABLE notifications (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    type VARCHAR(32) NOT NULL,
    title VARCHAR(512) NOT NULL,
    body TEXT,
    data JSONB,
    channel VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    sent_at TIMESTAMPTZ,
    read_at TIMESTAMPTZ
);

CREATE INDEX idx_notifications_user ON notifications (user_id, created_at DESC);

CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    action VARCHAR(128) NOT NULL,
    entity_type VARCHAR(128) NOT NULL,
    entity_id BIGINT,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(64),
    user_agent VARCHAR(512),
    timestamp TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_audit_logs_entity ON audit_logs (entity_type, entity_id);

CREATE TABLE system_config (
    id BIGINT PRIMARY KEY,
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value JSONB NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by_user_id BIGINT REFERENCES users (id)
);

CREATE TABLE feature_flags (
    id BIGINT PRIMARY KEY,
    feature_name VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    rules JSONB,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE system_metrics (
    id BIGINT PRIMARY KEY,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DOUBLE PRECISION NOT NULL,
    dimensions JSONB,
    timestamp TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_system_metrics_name_time ON system_metrics (metric_name, timestamp DESC);

CREATE TABLE reports (
    id BIGINT PRIMARY KEY,
    report_type VARCHAR(32) NOT NULL,
    format VARCHAR(32) NOT NULL,
    parameters JSONB,
    file_url VARCHAR(1024),
    generated_by_profile_id BIGINT,
    generated_at TIMESTAMPTZ NOT NULL,
    period_start DATE,
    period_end DATE
);

ALTER TABLE appointments
    ADD CONSTRAINT fk_appointments_emergency FOREIGN KEY (emergency_id) REFERENCES emergencies (id);
