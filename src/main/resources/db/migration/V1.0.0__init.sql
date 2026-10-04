CREATE TABLE short_urls (
    code TEXT PRIMARY KEY,
    original_url TEXT NOT NULL,
    custom_alias TEXT UNIQUE,
    expires_at TEXT,
    deleted_at TEXT DEFAULT NULL
);

CREATE TABLE forbidden_words (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    word TEXT NOT NULL UNIQUE
);
