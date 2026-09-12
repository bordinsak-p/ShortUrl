CREATE TABLE short_urls (
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    custom_alias TEXT UNIQUE,
    created_at TEXT  DEFAULT CURRENT_TIMESTAMP,
    deleted_at TEXT DEFAULT NULL
);

CREATE TABLE forbidden_words (
    id INTEGER PRIMARY KEY,
    word TEXT NOT NULL UNIQUE
);