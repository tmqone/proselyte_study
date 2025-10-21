-- liquibase formatted sql

-- changeset tmq:001
-- comment: initialize tables
CREATE TABLE writers (
    id BIGINT PRIMARY KEY UNIQUE GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE TABLE posts (
    id BIGINT PRIMARY KEY UNIQUE GENERATED ALWAYS AS IDENTITY,
    content TEXT NOT NULL,
    writer_id BIGINT NOT NULL REFERENCES writers(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE labels (
    id BIGINT PRIMARY KEY UNIQUE GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE posts_labels (
    primary key (post_id, label_id),
    post_id BIGINT REFERENCES posts(id) ON DELETE SET NULL,
    label_id BIGINT REFERENCES labels(id) ON DELETE SET NULL
);

CREATE INDEX idx_posts_writer_id ON posts(writer_id);
CREATE INDEX idx_posts_labels ON posts_labels(post_id, label_id);
