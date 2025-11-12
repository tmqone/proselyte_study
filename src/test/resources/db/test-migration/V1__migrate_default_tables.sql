CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100),
                       role VARCHAR(20)
);

CREATE TABLE files (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       file_path VARCHAR(256) NOT NULL,
                       is_deleted boolean default false
);

CREATE TABLE events (
                        id SERIAL PRIMARY KEY ,
                        user_id SERIAL NOT NULL REFERENCES users(id) ON DELETE NO ACTION,
                        file_id SERIAL NOT NULL REFERENCES files(id) ON DELETE no action,
                        action VARCHAR(10) NOT NULL
);

CREATE INDEX idx_event_user ON events(user_id);
CREATE INDEX idx_event_file ON events(file_id);
