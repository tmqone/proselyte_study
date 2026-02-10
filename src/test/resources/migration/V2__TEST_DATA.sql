INSERT INTO users (username, password, role, status) VALUES
('ivan',  '{noop}ivan',  'USER',    'ACTIVE'),
('maria', '{noop}maria', 'MANAGER', 'ACTIVE'),
('admin', '{noop}admin', 'ADMIN',   'ACTIVE');

INSERT INTO files (name, location, status) VALUES
('report.pdf',   's3://bucket/report.pdf',   'ACTIVE'),
('avatar.png',   's3://bucket/avatar.png',   'ACTIVE'),
('archive.zip',  's3://bucket/archive.zip',  'ARCHIVED'),
('notes.txt',    's3://bucket/notes.txt',    'ACTIVE');

INSERT INTO events (user_id, file_id, status) VALUES
                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = 'report.pdf'),
                                                   'CREATED'),

                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = 'avatar.png'),
                                                   'CREATED'),

                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = 'report.pdf'),
                                                   'DOWNLOADED');

INSERT INTO events (user_id, file_id, status) VALUES
    ((SELECT id FROM users WHERE username = 'maria'),
     (SELECT id FROM files WHERE name = 'notes.txt'),
     'CREATED');

INSERT INTO events (user_id, file_id, status)
SELECT (SELECT id FROM users WHERE username = 'admin'), f.id, 'CREATED'
FROM files f;