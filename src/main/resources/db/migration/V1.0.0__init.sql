CREATE TABLE short_urls (
    id INTEGER PRIMARY KEY,
    original_url TEXT NOT NULL,
    created_at TEXT  DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE forbidden_words (
    id INTEGER PRIMARY KEY,
    word TEXT NOT NULL UNIQUE
);