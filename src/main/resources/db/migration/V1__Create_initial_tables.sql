-- V1__init.sql
CREATE TYPE role_enum    AS ENUM ('PATIENT','DOCTOR','ADMIN');
CREATE TYPE status_enum  AS ENUM ('BOOKED','CANCELLED','DONE');

CREATE TABLE users (
                       id          BIGSERIAL PRIMARY KEY,
                       email       VARCHAR(255) NOT NULL UNIQUE,
                       password    VARCHAR(255) NOT NULL,
                       role        role_enum    NOT NULL,
                       created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                       updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE doctor_profiles (
                                 id          BIGSERIAL PRIMARY KEY,
                                 user_id     BIGINT  NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                                 full_name   VARCHAR(255) NOT NULL,
                                 specialty   VARCHAR(100) NOT NULL,
                                 description TEXT,
                                 created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE doctor_slots (
                              id          BIGSERIAL PRIMARY KEY,
                              doctor_id   BIGINT  NOT NULL REFERENCES doctor_profiles(id) ON DELETE CASCADE,
                              start_time  TIMESTAMPTZ NOT NULL,
                              end_time    TIMESTAMPTZ NOT NULL,
                              is_booked   BOOLEAN NOT NULL DEFAULT FALSE,
                              created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              CHECK (start_time < end_time),
                              UNIQUE (doctor_id, start_time)
);

CREATE TABLE appointments (
                              id          BIGSERIAL PRIMARY KEY,
                              patient_id  BIGINT NOT NULL REFERENCES users(id)          ON DELETE CASCADE,
                              slot_id     BIGINT NOT NULL REFERENCES doctor_slots(id)   ON DELETE CASCADE,
                              status      status_enum NOT NULL DEFAULT 'BOOKED',
                              created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              UNIQUE (slot_id)
);

-- индексы
CREATE INDEX idx_profile_specialty ON doctor_profiles(specialty);
CREATE INDEX idx_slot_free        ON doctor_slots(doctor_id, is_booked);
