package com.tmq.sql;

public class PostRepositorySql {
    public static final String FIND_ALL_SQL = """
            SELECT * FROM posts
            WHERE status != 'DELETED'
            """;

    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM posts
            WHERE id = ? and status != 'DELETED'
            """;

    public static final String FIND_BY_WRITER_SQL = """
            SELECT * FROM posts
            WHERE writer_id = ? AND status != 'DELETED';
            """;

    public static final String SAVE_POST_SQL = """
            INSERT INTO posts(content, writer_id, created_at, updated_at, status)
            VALUES (?, ?, ?, ?, ?)
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
