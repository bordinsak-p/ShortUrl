CREATE TABLE short_urls (
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE ,
    original_url TEXT NOT NULL,
    created_at TEXT  DEFAULT CURRENT_TIMESTAMP
);