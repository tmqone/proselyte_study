CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE ,
                       password VARCHAR(2048) NOT NULL,
                       role VARCHAR(32) NOT NULL,
                       status VARCHAR(50) NOT NULL
);

CREATE TABLE files (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       location VARCHAR(500) NOT NULL,
                       status VARCHAR(50) NOT NULL
);

CREATE TABLE events (
                        id SERIAL PRIMARY KEY,
                        user_id INT,
                        file_id INT,
                        status VARCHAR(50) NOT NULL,
                        timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (file_id) REFERENCES files(id)
);

