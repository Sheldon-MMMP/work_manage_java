-- PostgreSQL 建表脚本（与 MyBatis-Plus 实体一致）
-- 执行前请确保数据库已创建，如: CREATE DATABASE manage_work;

-- 1. 用户表（无外键，最先创建）
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    avatar          VARCHAR(255),
    created_at      TIMESTAMP(6),
    updated_at      TIMESTAMP(6)
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 2. 文件夹表
CREATE TABLE IF NOT EXISTS folders (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    parent_id       BIGINT,
    name            VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP(6),
    updated_at      TIMESTAMP(6),
    CONSTRAINT fk_folders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_folders_parent FOREIGN KEY (parent_id) REFERENCES folders(id)
);
CREATE INDEX IF NOT EXISTS idx_folders_user_id ON folders(user_id);
CREATE INDEX IF NOT EXISTS idx_folders_parent_id ON folders(parent_id);

-- 3. 清单表
CREATE TABLE IF NOT EXISTS task_lists (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    folder_id       BIGINT,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP(6),
    updated_at      TIMESTAMP(6),
    CONSTRAINT fk_task_lists_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_task_lists_folder FOREIGN KEY (folder_id) REFERENCES folders(id)
);
CREATE INDEX IF NOT EXISTS idx_task_lists_user_id ON task_lists(user_id);
CREATE INDEX IF NOT EXISTS idx_task_lists_folder_id ON task_lists(folder_id);

-- 4. 任务表
CREATE TABLE IF NOT EXISTS tasks (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    task_list_id    BIGINT NOT NULL,
    parent_id       BIGINT,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    status          VARCHAR(255) NOT NULL,
    is_completed    BOOLEAN NOT NULL DEFAULT FALSE,
    start_time      TIMESTAMP(6),
    end_time        TIMESTAMP(6),
    created_at      TIMESTAMP(6),
    updated_at      TIMESTAMP(6),
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_tasks_task_list FOREIGN KEY (task_list_id) REFERENCES task_lists(id),
    CONSTRAINT fk_tasks_parent FOREIGN KEY (parent_id) REFERENCES tasks(id)
);
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON tasks(user_id);
CREATE INDEX IF NOT EXISTS idx_tasks_task_list_id ON tasks(task_list_id);
CREATE INDEX IF NOT EXISTS idx_tasks_parent_id ON tasks(parent_id);

-- 5. 时间记录表
CREATE TABLE IF NOT EXISTS time_records (
    id              BIGSERIAL PRIMARY KEY,
    task_id         BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    start_time      TIMESTAMP(6) NOT NULL,
    end_time        TIMESTAMP(6) NOT NULL,
    created_at      TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_time_records_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_time_records_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IF NOT EXISTS idx_time_records_user_id ON time_records(user_id);
CREATE INDEX IF NOT EXISTS idx_time_records_task_id ON time_records(task_id);
CREATE INDEX IF NOT EXISTS idx_time_records_start_time ON time_records(start_time);

-- 6. 纪念日表
CREATE TABLE IF NOT EXISTS anniversaries (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    cover_url           VARCHAR(255),
    title               VARCHAR(255) NOT NULL,
    description         TEXT,
    anniversary_date    DATE NOT NULL,
    created_at          TIMESTAMP(6),
    updated_at          TIMESTAMP(6),
    CONSTRAINT fk_anniversaries_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IF NOT EXISTS idx_anniversaries_user_id ON anniversaries(user_id);
