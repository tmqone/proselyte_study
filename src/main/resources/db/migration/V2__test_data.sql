-- liquibase formatted sql

-- changeset tmq:002
-- comment: migrate test data
INSERT INTO writers (first_name, last_name) VALUES
                                                ('Haruki', 'Murakami'),
                                                ('Joan', 'Didion'),
                                                ('George', 'Orwell'),
                                                ('Ursula', 'Le Guin'),
                                                ('Gabriel', 'Garcia Marquez'),
                                                ('Virginia', 'Woolf'),
                                                ('Toni', 'Morrison'),
                                                ('Mikhail', 'Bulgakov');

INSERT INTO labels (name) VALUES
                              ('fiction'),
                              ('non-fiction'),
                              ('memoir'),
                              ('essay'),
                              ('sci-fi'),
                              ('fantasy'),
                              ('magic-realism'),
                              ('russian-lit'),
                              ('modernism'),
                              ('classic');


WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Ночной Токио, радио шепчет, и герои ищут себя.',
                   (SELECT id FROM writers WHERE first_name='Haruki' AND last_name='Murakami'),
                   NOW() - INTERVAL '90 days',
                   NOW() - INTERVAL '89 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('fiction','modernism','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Память — это способ рассказать себе, как все было.',
                   (SELECT id FROM writers WHERE first_name='Joan' AND last_name='Didion'),
                   NOW() - INTERVAL '60 days',
                   NOW() - INTERVAL '59 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('non-fiction','essay','memoir');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Свобода — это право говорить то, чего не хотят слышать.',
                   (SELECT id FROM writers WHERE first_name='George' AND last_name='Orwell'),
                   NOW() - INTERVAL '45 days',
                   NOW() - INTERVAL '44 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('fiction','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Воображение — не бегство от реальности, а способ в неё войти.',
                   (SELECT id FROM writers WHERE first_name='Ursula' AND last_name='Le Guin'),
                   NOW() - INTERVAL '30 days',
                   NOW() - INTERVAL '29 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('sci-fi','fantasy');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Сто лет одиночества растворяются в запахе дождя.',
                   (SELECT id FROM writers WHERE first_name='Gabriel' AND last_name='Garcia Marquez'),
                   NOW() - INTERVAL '28 days',
                   NOW() - INTERVAL '27 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('fiction','magic-realism','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Комната, где можно быть собой и писать.',
                   (SELECT id FROM writers WHERE first_name='Virginia' AND last_name='Woolf'),
                   NOW() - INTERVAL '20 days',
                   NOW() - INTERVAL '19 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('modernism','essay','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Память — дом, в котором живут призраки.',
                   (SELECT id FROM writers WHERE first_name='Toni' AND last_name='Morrison'),
                   NOW() - INTERVAL '14 days',
                   NOW() - INTERVAL '13 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('fiction','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Рукописи не горят.',
                   (SELECT id FROM writers WHERE first_name='Mikhail' AND last_name='Bulgakov'),
                   NOW() - INTERVAL '10 days',
                   NOW() - INTERVAL '9 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('russian-lit','fiction','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Коты, колодцы и музыка — как карта памяти.',
                   (SELECT id FROM writers WHERE first_name='Haruki' AND last_name='Murakami'),
                   NOW() - INTERVAL '7 days',
                   NOW() - INTERVAL '6 days',
                   'DELETED'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('fiction','modernism');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Мы рассказываем истории, чтобы жить.',
                   (SELECT id FROM writers WHERE first_name='Joan' AND last_name='Didion'),
                   NOW() - INTERVAL '5 days',
                   NOW() - INTERVAL '4 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('essay','non-fiction');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Ясность — главный признак писательства.',
                   (SELECT id FROM writers WHERE first_name='George' AND last_name='Orwell'),
                   NOW() - INTERVAL '3 days',
                   NOW() - INTERVAL '2 days',
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('essay','classic');

WITH p AS (
    INSERT INTO posts (content, writer_id, created_at, updated_at, status)
        VALUES (
                   'Язык создаёт миры так же, как карты — территории.',
                   (SELECT id FROM writers WHERE first_name='Ursula' AND last_name='Le Guin'),
                   NOW() - INTERVAL '1 day',
                   NOW(),
                   'ACTIVE'
               )
        RETURNING id
)
INSERT INTO posts_labels (post_id, label_id)
SELECT p.id, l.id
FROM p CROSS JOIN labels l
WHERE l.name IN ('sci-fi','fantasy','essay');