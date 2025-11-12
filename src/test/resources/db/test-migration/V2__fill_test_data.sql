INSERT INTO users (username, password, role)
VALUES ('user', '$2a$12$yZgSjzbJOKUPEDQoITro6OP3yKnL5vSssRw6vigKIzLM8LFhgIsKG', 'USER');

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$12$HcmU7tBCbem8Cv66/us2ZexFZCLKVcoyoeng8VD0hgID/Lpw.eQ46', 'ADMIN');

INSERT INTO files (name, file_path, is_deleted)
VALUES ('first_test', '${test.file.path}/1/first_test', 'false');

INSERT INTO files (name, file_path, is_deleted)
VALUES ('second_test', '${test.file.path}/1/second_test', 'false');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 1, 'UPLOAD');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 1, 'GET');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 1, 'DOWNLOAD');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 2, 'UPLOAD');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 2, 'GET');

INSERT INTO events (user_id, file_id, action)
VALUES (1, 2, 'DOWNLOAD');

