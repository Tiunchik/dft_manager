drop table if exists tasks;

CREATE TABLE IF NOT EXISTS tasks
(
    code        VARCHAR(10) PRIMARY KEY,
    title       TEXT        DEFAULT ''                  NOT NULL,
    "content"   TEXT        DEFAULT ''                  NOT NULL,
    creator_id  BIGINT                                  NOT NULL,
    checker_id  BIGINT                                  NULL,
    executor_id BIGINT                                  NULL,
    deadline_at TIMESTAMP                               NULL,
    status_id   BIGINT                                  NOT NULL,
    priority    VARCHAR(16) DEFAULT 'MEDIUM'            NOT NULL,
    created_at  TIMESTAMP   DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_at  TIMESTAMP   DEFAULT (CURRENT_TIMESTAMP) NOT NULL
);
ALTER TABLE tasks
    ADD CONSTRAINT tasks_code_unique UNIQUE (code);

ALTER TABLE tasks ALTER COLUMN created_at TYPE TIMESTAMP, ALTER COLUMN created_at SET DEFAULT (CURRENT_TIMESTAMP);
ALTER TABLE tasks ALTER COLUMN updated_at TYPE TIMESTAMP, ALTER COLUMN updated_at SET DEFAULT (CURRENT_TIMESTAMP);