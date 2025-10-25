package com.tmq.sql;

public class PostRepositorySql {
    public static final String FIND_ALL_SQL = """
            select p.id                      as post_id,
                   p.content,
                   p.created_at,
                   p.updated_at,
                   p.status,
                   w.id                        as writer_id,
                   w.first_name,
                   w.last_name,
                   l.id                        as label_id,
                   l.name                     as label_name
            from posts p
                     join writers w on p.writer_id = w.id
                     join posts_labels pl on p.id = pl.post_id
                     join labels l on l.id = pl.label_id
            where p.status != 'DELETED'
            """;

    public static final String FIND_BY_ID_SQL = """
            select p.id                      as post_id,
                   p.content,
                   p.created_at,
                   p.updated_at,
                   p.status,
                   w.id                        as writer_id,
                   w.first_name,
                   w.last_name,
                   l.id                        as label_id,
                   l.name                     as label_name
            from posts p
                     join writers w on p.writer_id = w.id
                     join posts_labels pl on p.id = pl.post_id
                     join labels l on l.id = pl.label_id
            where p.id = ? and p.status != 'DELETED'
            """;

    public static final String FIND_BY_WRITER_SQL = """
            select p.id                      as post_id,
                   p.content,
                   p.created_at,
                   p.updated_at,
                   p.status,
                   w.id                        as writer_id,
                   w.first_name,
                   w.last_name,
                   l.id                        as label_id,
                   l.name                     as label_name
            from posts p
                     join writers w on p.writer_id = w.id
                     join posts_labels pl on p.id = pl.post_id
                     join labels l on l.id = pl.label_id
            where w.id = ? and p.status != 'DELETED'
            """;

    public static final String SAVE_POST_SQL = """
            INSERT INTO posts(content, writer_id, created_at, updated_at, status)
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String DELETE_LABEL_FROM_POST = """
            delete from posts_labels
            where post_id = ?
            """;

    public static final String DELETE_BY_ID_SQL = """
            UPDATE posts SET status = 'DELETED' WHERE id = ?;
            """;

    public static final String UPDATE_SQL = """
            UPDATE posts
            set content = ?, writer_id = ?, updated_at = ?, status = ?
            WHERE id = ? and status != 'DELETED'
            """;
}
