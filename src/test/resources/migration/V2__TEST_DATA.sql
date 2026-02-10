INSERT INTO users (username, password, role, status) VALUES
('ivan',  'ivan',  'USER',    'ACTIVE'),
('maria', 'maria', 'MANAGER', 'ACTIVE'),
('admin', 'admin', 'ADMIN',   'ACTIVE');

INSERT INTO files (name, location, status) VALUES
('1.pdf',   '1',   'ACTIVE'),
('2.png',   '2',   'ACTIVE'),
('3.zip',  '3',  'ARCHIVED'),
('4.txt',    '4',    'ACTIVE');

INSERT INTO events (user_id, file_id, status) VALUES
                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = '1.pdf'),
                                                   'CREATED'),

                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = '2.png'),
                                                   'CREATED'),

                                                  ((SELECT id FROM users WHERE username = 'ivan'),
                                                   (SELECT id FROM files WHERE name = '3.zip'),
                                                   'CREATED');

INSERT INTO events (user_id, file_id, status) VALUES
    ((SELECT id FROM users WHERE username = 'maria'),
     (SELECT id FROM files WHERE name = 'notes.txt'),
     'CREATED');

INSERT INTO events (user_id, file_id, status)
SELECT (SELECT id FROM users WHERE username = 'admin'), f.id, 'CREATED'
FROM files f;