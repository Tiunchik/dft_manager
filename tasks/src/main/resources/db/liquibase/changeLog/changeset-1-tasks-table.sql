--changeset changeset-1-tasks-table-1
-- truncate table databasechangelog;

CREATE TABLE IF NOT EXISTS tasks
(
    id    serial primary key,
    title text
);

--changeset changeset-1-tasks-table-1
ALTER TABLE tasks
    RENAME COLUMN id TO code;
ALTER TABLE tasks
    ALTER COLUMN code TYPE varchar(10) using code::varchar(10);
ALTER TABLE tasks
    ALTER COLUMN code SET NOT NULL;
ALTER TABLE tasks
    ADD CONSTRAINT tasks_code__unique UNIQUE (code);

ALTER TABLE tasks
    ALTER column title SET NOT NULL;
ALTER TABLE tasks
    add column content text not null default '';

ALTER TABLE tasks
    add column creator_id int;
UPDATE tasks
SET creator_id = 0;
ALTER TABLE tasks
    ALTER column creator_id SET NOT NULL;

ALTER TABLE tasks
    add column checker_id int default null;
ALTER TABLE tasks
    add column executor_id int default null;
ALTER TABLE tasks
    add column deadline_at TIMESTAMP WITHOUT TIME ZONE default null;