-- ─────────────────────────────────────────────
--  BOOKSHELF APP — schema.sql
--  src/main/resources/schema.sql
--  Run this manually in MySQL Workbench ONCE:
--    CREATE DATABASE bookshelf_db;
--    USE bookshelf_db;
--    then paste and run this file.
--  After that, JPA (ddl-auto=update) maintains it.
-- ─────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(150),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    auth_provider VARCHAR(10)  NOT NULL DEFAULT 'LOCAL',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login    TIMESTAMP    NULL
);

CREATE TABLE IF NOT EXISTS user_sessions (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    token_hash  VARCHAR(512) NOT NULL UNIQUE,
    expires_at  TIMESTAMP    NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS otp_codes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    code        VARCHAR(10)  NOT NULL,
    expires_at  TIMESTAMP    NOT NULL,
    used        TINYINT(1)   NOT NULL DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    ol_book_id  VARCHAR(100),
    title       VARCHAR(500) NOT NULL,
    author      VARCHAR(300),
    genre       VARCHAR(200),
    total_pages INT          DEFAULT 0,
    cover_url   VARCHAR(500),
    ol_url      VARCHAR(500),
    added_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reading_progress (
    id            BIGINT    AUTO_INCREMENT PRIMARY KEY,
    book_id       BIGINT    NOT NULL UNIQUE,
    pages_read    INT       NOT NULL DEFAULT 0,
    is_completed  TINYINT(1) NOT NULL DEFAULT 0,
    last_updated  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_book FOREIGN KEY (book_id)
        REFERENCES books(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS saved_entries (
    id          BIGINT        AUTO_INCREMENT PRIMARY KEY,
    book_id     BIGINT        NOT NULL,
    entry_type  VARCHAR(50)   NOT NULL DEFAULT 'QUOTE',
    content     TEXT          NOT NULL,
    page_ref    VARCHAR(20),
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_entry_book FOREIGN KEY (book_id)
        REFERENCES books(id) ON DELETE CASCADE
);
