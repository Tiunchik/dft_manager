CREATE TABLE IF NOT EXISTS "tasks-change-commits"
(
    uuid       uuid PRIMARY KEY,
    created_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    creator_id BIGINT                                NOT NULL,
    task_code  VARCHAR(10)                           NOT NULL,
    CONSTRAINT "fk_tasks-change-commits_task_code__code" FOREIGN KEY (task_code) REFERENCES tasks (code) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE TABLE IF NOT EXISTS "tasks-change-events"
(
    field_name  TEXT NOT NULL,
    old_value   TEXT NOT NULL,
    new_value   TEXT NOT NULL,
    commit_uuid uuid NOT NULL,
    CONSTRAINT "fk_tasks-change-events_commit_uuid__uuid" FOREIGN KEY (commit_uuid) REFERENCES "tasks-change-commits" (uuid) ON DELETE RESTRICT ON UPDATE RESTRICT
);