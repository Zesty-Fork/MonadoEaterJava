CREATE TABLE IF NOT EXISTS webpage (
    id INTEGER PRIMARY KEY AUTOINCREMENT
    ,url TEXT NOT NULL
    ,html BLOB NOT NULL
);