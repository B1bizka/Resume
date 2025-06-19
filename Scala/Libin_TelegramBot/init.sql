CREATE TABLE reminders (
    id SERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    chat_id BIGINT NOT NULL,
    message TEXT NOT NULL
);